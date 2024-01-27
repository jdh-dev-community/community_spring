package com.jdh.community_spring.domain.post.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.exception.NotFoundException;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.PostCreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;



  private final String baseUrl = "/api/v1";

  @DisplayName("게시글 생성")
  @Nested
  class CreatePost {
    private final String url = baseUrl + "/post";

    @Test
    public void 요청의_Body가_유효할경우_201응답() throws Exception {
      String requestBody = createDummyBody(null);
      int postId = 1;

      PostResDto resDto = new PostResDto(postId, "제목", "컨텐츠", "카테고리", "작성자", 0, LocalDateTime.now());
      when(postService.createPost(any(PostCreateReqDto.class))).thenReturn(resDto);

      postAndVerify(requestBody)
              .andExpect(status().isCreated())
              .andExpect(jsonPath("$.postId", Matchers.equalTo(postId)));
    }

    @Test
    public void 요청의_Body에_필수값이누락된경우_400을응답() throws Exception {
      String requestBody = createDummyBody("title");

      postAndVerify(requestBody)
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.path", Matchers.equalTo(url)))
              .andExpect(jsonPath("$.httpStatus", Matchers.equalTo(HttpStatus.BAD_REQUEST.name())))
              .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
              .andExpect(jsonPath("$.message", Matchers.notNullValue()));
    }

    private ResultActions postAndVerify(String body) throws Exception {
      return mockMvc.perform(post(url)
              .contentType(MediaType.APPLICATION_JSON)
              .content(body));
    }

    private String createDummyBody(String deleteId) throws JsonProcessingException {

      Map<String, String> dummy = new HashMap<>();
      dummy.put("title", "제목");
      dummy.put("content", "내용");
      dummy.put("category", "카테고리");
      dummy.put("creator", "생성자");
      dummy.put("password", "1234");

      if (deleteId != null) dummy.remove(deleteId);
      String json = objectMapper.writeValueAsString(dummy);

      return json;
    }
  }

  @DisplayName("게시글 목록 조회")
  @Nested
  class GetPostList {
    private final int TOTAL_COUNT = 50;

    @Test
    public void 요청에_쿼리미함포시_기본값사용하여_성공응답반환() throws Exception {
      String url = baseUrl + "/post";

      ListReqDto dto = new ListReqDto(1, 10, "createdAt", "desc");
      when(postService.getPostList(dto))
              .thenReturn(createDummy(dto.getSize(), TOTAL_COUNT));

      mockMvc.perform(get(url))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(TOTAL_COUNT)))
              .andExpect(jsonPath("$.content.length()", Matchers.lessThanOrEqualTo(dto.getSize())));
    }

    @Test
    public void 요청에_쿼리포함시_해당값사용하여_성공응답반환() throws Exception {
      int page = 2;
      int pageSize = 5;
      String sortBy = "createdAt";
      String orderBy = "desc";

      String url = new StringBuilder(baseUrl + "/post")
              .append("?page=" + page)
              .append("&size=" + pageSize)
              .append("&sortBy=" + sortBy)
              .append("&orderBy=" + orderBy)
              .toString();

      ListReqDto dto = new ListReqDto(page, pageSize, sortBy, orderBy);
      when(postService.getPostList(dto)).thenReturn(createDummy(pageSize, TOTAL_COUNT));

      mockMvc.perform(get(url))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(TOTAL_COUNT)))
              .andExpect(jsonPath("$.content.length()", Matchers.lessThanOrEqualTo(pageSize)));
    }

    @Test
    public void 요청에_유효하지않은값이포함시_400응답반환() throws Exception {
      int invalidPage = 0;
      int invalidSize = 0;

      String url = baseUrl + "/post?page=" + invalidPage + "&size=" + invalidSize;

      mockMvc.perform(get(url))
              .andExpect(status().isBadRequest())
              .andExpect(jsonPath("$.path", Matchers.equalTo(baseUrl + "/post")))
              .andExpect(jsonPath("$.httpStatus", Matchers.equalTo(HttpStatus.BAD_REQUEST.name())))
              .andExpect(jsonPath("$.timestamp", Matchers.notNullValue()))
              .andExpect(jsonPath("$.message", Matchers.notNullValue()));

    }

    private ListResDto<PostResDto> createDummy(int size, int totalElements) {
      List<PostResDto> list = IntStream.rangeClosed(1, size)
              .mapToObj((i) -> new PostResDto(i,"제목" + i, "이건 더미 데이터", "테스트", "테스트", 0,LocalDateTime.now()))
              .collect(Collectors.toList());

      return new ListResDto<>(totalElements, list);
    }
  }

  @DisplayName("게시글 상세 조회")
  @Nested
  class GetPost {
    private final String url = baseUrl + "/post";


    @Test
    public void 요청에_유효한id포함시_성공응답반환() throws Exception {
      String validId = "1";

      when(postService.getPost(validId)).thenReturn(createDummyPost(validId));

      mockMvc.perform(get(url + "/" + validId))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.postId", Matchers.equalTo(Integer.parseInt(validId))));
    }

    @Test
    public void 요청에_유효하지않은id포함시_400응답반환() throws Exception {
      String validId = "invalidId";

      when(postService.getPost(String.valueOf(validId))).thenThrow(IllegalArgumentException.class);

      mockMvc.perform(get(url + "/" + validId))
              .andExpect(status().isBadRequest());
    }

    @Test
    public void 요청에_포함된id에_매칭되는게시글이없을시_404응답반환() throws Exception {
      String notMatchedId = "1000000";

      when(postService.getPost(String.valueOf(notMatchedId))).thenThrow(NotFoundException.class);

      mockMvc.perform(get(url + "/" + notMatchedId))
              .andExpect(status().isNotFound());
    }


    private PostResDto createDummyPost(String postId) throws JsonProcessingException {
      PostResDto dto = new PostResDto(
              Long.parseLong(postId),
              "t",
              "t",
              "t",
              "2",
              1,
              LocalDateTime.now()
      );

      return dto;
    }

  }
}


