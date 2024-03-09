package com.jdh.community_spring.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdh.community_spring.common.constant.CommentStatusKey;
import com.jdh.community_spring.common.constant.PostCategory;
import com.jdh.community_spring.common.filter.TokenFilter;
import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.CommentStatus;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.repository.CommentStatusRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentControllerTest {

  private final String dummyPassword = "1234";

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private SimpleEncrypt simpleEncrypt;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private CommentStatusRepository commentStatusRepository;

  @Autowired
  private InMemoryDBProvider inMemoryDBProvider;

  @Autowired
  private WebApplicationContext context;

  private String baseUrl;
  private Post post;
  private MockMvc mockMvc;

  private static boolean isInitialized = false;

  @BeforeEach
  public void setup() {
    if (isInitialized == false) {
      commentStatusRepository.save(createCommentStatus(CommentStatusKey.ACTIVE));
      commentStatusRepository.save(createCommentStatus(CommentStatusKey.INACTIVE));
      isInitialized = true;
    }

    post = postRepository.save(createPost("post"));

    baseUrl = "/api/v1/post/" + post.getPostId() + "/comment";

    TokenFilter tokenFilter = new TokenFilter(inMemoryDBProvider);
    mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .addFilter(tokenFilter, "/api/v1/post/*")
            .build();
  }

  @AfterEach
  public void cleanup() {
    postRepository.deleteAll();
    baseUrl = "";
  }

  @Nested
  class 댓글생성_테스트 {
    private Map<String, String> defaultRequest;

    @BeforeEach
    public void setup() {
      this.defaultRequest = Map.of(
              "content", "it is comment",
              "creator", "me",
              "password", "1234",
              "parentId", "null",
              "status", "inactive"

      );
    }

    @Test
    public void 요청_데이터가_유효한_경우_생성된_댓글과_201_응답() throws Exception {
      Map<String, String> validRequest = defaultRequest;
      String validBody = objectMapper.writeValueAsString(validRequest);
      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.commentId").exists())
              .andExpect(jsonPath("$.content", Matchers.equalTo(validRequest.get("content"))))
              .andExpect(jsonPath("$.creator", Matchers.equalTo(validRequest.get("creator"))))
              .andExpect(jsonPath("$.createdAt").exists())
              .andExpect(jsonPath("$.childrenCommentCount", Matchers.equalTo(0)))
              .andExpect(jsonPath("$.postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.status", Matchers.equalTo(validRequest.get("status"))))
              .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void 요청_데이터에_부모댓글id가_없는_경우_생성된_댓글과_201_응답() throws Exception {
      Map<String, String> validRequest = new HashMap<>(defaultRequest);
      validRequest.remove("parentId");
      String validBody = objectMapper.writeValueAsString(validRequest);

      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.commentId").exists())
              .andExpect(jsonPath("$.content", Matchers.equalTo(validRequest.get("content"))))
              .andExpect(jsonPath("$.creator", Matchers.equalTo(validRequest.get("creator"))))
              .andExpect(jsonPath("$.createdAt").exists())
              .andExpect(jsonPath("$.childrenCommentCount", Matchers.equalTo(0)))
              .andExpect(jsonPath("$.postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.status", Matchers.equalTo(validRequest.get("status"))))
              .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void 요청_데이터에_댓글상태가_없으면_active로_생성된_댓글과_201_응답() throws Exception {
      Map<String, String> validRequest = new HashMap<>(defaultRequest);
      validRequest.remove("status");
      String validBody = objectMapper.writeValueAsString(validRequest);

      postAndVerify(validBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.commentId").exists())
              .andExpect(jsonPath("$.content", Matchers.equalTo(validRequest.get("content"))))
              .andExpect(jsonPath("$.creator", Matchers.equalTo(validRequest.get("creator"))))
              .andExpect(jsonPath("$.createdAt").exists())
              .andExpect(jsonPath("$.childrenCommentCount", Matchers.equalTo(0)))
              .andExpect(jsonPath("$.postId", Matchers.equalTo((int) post.getPostId())))
              .andExpect(jsonPath("$.status", Matchers.equalTo(CommentStatusKey.ACTIVE.getCommentStatus())))
              .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void 요청_데이터에_필수값이_누락된_경우_400_응답() throws Exception {
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.remove("content");
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 부모댓글id가_유효하지_않은_경우_404_응답을_반환() throws Exception {
      Map<String, String> invalidRequest = new HashMap<>(defaultRequest);
      invalidRequest.put("parentId", "1000000");
      String invalidBody = objectMapper.writeValueAsString(invalidRequest);

      postAndVerify(invalidBody)
              .andExpect(status().isNotFound());
    }

    private ResultActions postAndVerify(String body) throws Exception {
      return mockMvc.perform(post(baseUrl)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body)
      );
    }

  }

  @Nested
  class 댓글목록_테스트 {
  }

  @Nested
  class 대댓글목록_테스트 {
  }

  @Nested
  class 댓글삭제_테스트 {
  }


  private CommentStatus createCommentStatus(CommentStatusKey status) {
    return CommentStatus.builder()
            .status(status)
            .build();
  }

  private Post createPost(String title) {
    return Post.builder()
            .title(title)
            .textContent("content")
            .category(PostCategory.AD)
            .password(simpleEncrypt.encrypt(dummyPassword))
            .creator("me")
            .build();
  }
}
