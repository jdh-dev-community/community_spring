package com.jdh.community_spring.domain.post.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdh.community_spring.common.constant.PostCategory;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private final String baseUrl = "/api/v1/post";

  @Nested
  class 게시글생성_테스트 {

    private Map<String, String> defaultRequest;

    @BeforeEach
    public void setup() {
      this.defaultRequest = Map.of(
              "title", "제목",
              "content", "it is contents",
              "creator", "me",
              "category", "question",
              "password", "1234"
      );
    }

    @Test
    public void 요청_데이터가_유효한_경우_생성된_게시글과_201_응답을_반환() throws Exception {
      Map<String, String> validRequest = defaultRequest;
      String validBody = objectMapper.writeValueAsString(validRequest);

      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.title", Matchers.equalTo(validRequest.get("title"))));
    }

    @Test
    public void 요청_데이터에_필수값이_누락된_경우_400_응답을_반환() throws Exception {
      String requiredField = "title";
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.remove(requiredField);
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody).andExpect(status().isBadRequest());
    }

    @Test
    public void 요청_데이터가_유효하지_않은_경우_400_응답을_반환() throws Exception {
      String invalidData = "It is an invalid data";
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.put("category", invalidData);
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody).andExpect(status().isBadRequest());

    }

    private ResultActions postAndVerify(String body) throws Exception {
      return mockMvc.perform(post(baseUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }
  }

  @Nested
  class 게시글상세_테스트 {
    private long savedPostId;

    @BeforeEach
    public void setup() {
      Post post = createPost(1);
      Post savedPost = postRepository.save(post);
      savedPostId = savedPost.getPostId();
    }

    @AfterEach
    public void cleanup() {
      postRepository.deleteAll();
      savedPostId = 0;
    }

    @Test
    public void 게시글_id에_해당하는_게시글이_존재하는_경우_게시글과_200_응답() throws Exception {
      String validId = String.valueOf(savedPostId);

      mockMvc.perform(get(baseUrl + "/" + validId))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.postId", Matchers.equalTo(Integer.parseInt(validId))))
              .andExpect(jsonPath("$.title").exists())
              .andExpect(jsonPath("$.content").exists())
              .andExpect(jsonPath("$.category").exists())
              .andExpect(jsonPath("$.viewCount").exists())
              .andExpect(jsonPath("$.comments").exists())
              .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void 게시글_id에_해당하는_게시글이_존재하지_않는_경우_EntityNotFoundException_발생_404_응답() throws Exception {
      String notMatchedId = String.valueOf(savedPostId + 1);

      mockMvc.perform(get(baseUrl + "/" + notMatchedId))
              .andExpect(status().isNotFound());
    }

    @Test
    public void 게시글_id가_유효하지_않는_경우_400_응답() throws Exception {
      String invalidId = "id";
      mockMvc.perform(get(baseUrl + "/" + invalidId))
              .andExpect(status().isBadRequest());
    }
  }

  @Nested
  class 게시글목록_테스트 {
    private final int LIST_COUNT = 40;

    @BeforeEach
    public void setup() {
      List<Post> posts = IntStream.rangeClosed(1, LIST_COUNT)
              .mapToObj((i) -> createPost(i))
              .collect(Collectors.toList());

      postRepository.saveAll(posts);
    }

    @AfterEach
    public void cleanup() {
      postRepository.deleteAll();
    }

    @Test
    public void 목록조회_성공시_전체_게시글_개수와_페이지_사이즈_만큼의_목록을_반환하고_200_응답() throws Exception {
      int pageSize = 3;

      mockMvc.perform((get(baseUrl + "?size=" + pageSize)))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(LIST_COUNT)))
              .andExpect(jsonPath("$.content.size()", Matchers.equalTo(pageSize)))
              .andExpect(jsonPath("$.content.[0].title").exists())
              .andExpect(jsonPath("$.content.[0].content").exists())
              .andExpect(jsonPath("$.content.[0].category").exists())
              .andExpect(jsonPath("$.content.[0].creator").exists())
              .andExpect(jsonPath("$.content.[0].viewCount").exists())
              .andExpect(jsonPath("$.content.[0].commentCount").exists())
              .andExpect(jsonPath("$.content.[0].createdAt").exists());
    }

    @Test
    public void 해당_페이지에_데이터가_없는_경우_() throws Exception {
      int page = 100000;

      mockMvc.perform((get(baseUrl + "?page=" + page)))
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(LIST_COUNT)))
              .andExpect(jsonPath("$.content.size()", Matchers.equalTo(0)));
    }

    @Test
    public void 페이지가_주어지지_않는_경우_1페이지를_기본으로_로딩하고_200_응답() throws Exception {
      int DEFAULT_PAGE_NUMBER = 1;

      MockHttpServletResponse responseWithPageNumber = mockMvc
              .perform(get(baseUrl + "?page=" + DEFAULT_PAGE_NUMBER))
              .andReturn()
              .getResponse();

      List<Integer> expectedContentIds = extractIds(responseWithPageNumber);


      MockHttpServletResponse responseWithoutPageNumber = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Integer> contentIds = extractIds(responseWithoutPageNumber);
      int status = responseWithoutPageNumber.getStatus();


      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 페이지_사이즈가_주어지지_않는_경우_사이즈10을_기본으로_로딩하고_200_응답() throws Exception {
      int DEFAULT_PAGE_SIZE = 10;

      MockHttpServletResponse responseWithPageSize = mockMvc
              .perform(get(baseUrl + "?size=" + DEFAULT_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Integer> expectedContentIds = extractIds(responseWithPageSize);

      MockHttpServletResponse responseWithoutPageSize = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Integer> contentIds = extractIds(responseWithoutPageSize);
      int status = responseWithoutPageSize.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 정렬기준이_주어지지_않는_경우_최신순으로_정렬하고_200_응답() throws Exception {
      String DEFAULT_SORT_BY = "recent";

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(baseUrl + "?sortBy=" + DEFAULT_SORT_BY))
              .andReturn()
              .getResponse();

      List<Integer> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Integer> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 오름차순_내림차순이_별도로_주어지지_않은_경우_내림차순_정렬하고_200_응답() throws Exception {
      String DEFAULT_ORDER_BY = "desc";

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(baseUrl + "?orderBy=" + DEFAULT_ORDER_BY))
              .andReturn()
              .getResponse();

      List<Integer> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(baseUrl))
              .andReturn()
              .getResponse();

      List<Integer> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    @Test
    public void 페이지_사이즈가_30개_이상인_경우_최대_30개까지만_반환하고_200_응답() throws Exception {
      int MAX_PAGE_SIZE = 30;
      int EXCEEDED_PAGE_SIZE = 60;

      MockHttpServletResponse responseWithSortBy = mockMvc
              .perform(get(baseUrl + "?size=" + MAX_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Integer> expectedContentIds = extractIds(responseWithSortBy);

      MockHttpServletResponse responseWithoutSortBy = mockMvc
              .perform(get(baseUrl + "?size=" + EXCEEDED_PAGE_SIZE))
              .andReturn()
              .getResponse();

      List<Integer> contentIds = extractIds(responseWithoutSortBy);
      int status = responseWithoutSortBy.getStatus();

      assertEquals(expectedContentIds, contentIds);
      assertEquals(HttpStatus.OK.value(), status);
    }

    private List<Integer> extractIds(MockHttpServletResponse response) throws Exception {
      JsonNode rootNode = objectMapper.readTree(response.getContentAsString());
      JsonNode contentNodes = rootNode.path("content");

      List<Integer> ids = new ArrayList<>();

      for (JsonNode node: contentNodes) {
        ids.add(node.path("postId").asInt());
      }

      return ids;
    }

  }

  private Post createPost(Integer suffix) {
    return Post.builder()
            .title("제목" + suffix)
            .textContent("content")
            .category(PostCategory.AD)
            .password("1234")
            .creator("me")
            .build();
  }
}



