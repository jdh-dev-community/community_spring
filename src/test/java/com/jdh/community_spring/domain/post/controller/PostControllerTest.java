package com.jdh.community_spring.domain.post.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListReqDto;
import org.springframework.data.domain.PageRequest;
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

  @Nested
  class GetPostList {
    private final String url = baseUrl + "/post";
    @Test
    public void RequestBody가유효할경우201을응답한다() throws Exception {
      String requestBody = createDummyBody(null);
      postAndVerify(requestBody)
              .andExpect(status().isCreated());
    }

    @Test
    public void RequestBody에필수값이누락된경우400을응답한다() throws Exception {
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

      if (deleteId != null) dummy.remove(deleteId);
      String json = objectMapper.writeValueAsString(dummy);

      return json;
    }
  }

  @Nested
  class CreatePost {
    private final int TOTAL_COUNT = 50;
    private final String url = baseUrl + "/post";
    
    @Test
    public void 쿼리에페이지와사이즈가포함되지않은경우에는기본값을사용해서200을응답한다() throws Exception {
      int page = 1;
      int pageSize = 10;

      when(postService.getPostList(PageRequest.of(page - 1, pageSize)))
              .thenReturn(createDummy(pageSize, TOTAL_COUNT));

      mockMvc.perform(get(url))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(TOTAL_COUNT)))
              .andExpect(jsonPath("$.content.length()", Matchers.lessThanOrEqualTo(pageSize)));
    }

    @Test
    public void 쿼리에페이지와사이즈가포함된경우에는쿼리를사용해서200을응답한다() throws Exception {
      int page = 2;
      int pageSize = 5;

      String path = new StringBuilder(url)
              .append("?page=" + page)
              .append("&size=" + pageSize)
              .toString();

      when(postService.getPostList(PageRequest.of(page - 1, pageSize)))
              .thenReturn(createDummy(pageSize, TOTAL_COUNT));

      mockMvc.perform(get(path))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.elementsCount", Matchers.equalTo(TOTAL_COUNT)))
              .andExpect(jsonPath("$.content.length()", Matchers.lessThanOrEqualTo(pageSize)));
    }

    private ListReqDto<Post> createDummy(int size, int totalElements) {
      List<Post> list = IntStream.rangeClosed(1, size)
              .mapToObj((i) -> new Post("제목" + i, "이건 더미 데이터", "테스트", "테스트"))
              .collect(Collectors.toList());

      ListReqDto<Post> dto = new ListReqDto<>(totalElements, list);
      return dto;
    }

  }
}
