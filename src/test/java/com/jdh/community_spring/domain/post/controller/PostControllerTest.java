package com.jdh.community_spring.domain.post.controller;


import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.ListReqDto;
import org.springframework.data.domain.PageRequest;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @Nested
  class 게시글목록api {
    private final int TOTAL_COUNT = 50;


    @Test
    public void 쿼리에페이지와사이즈가포함되지않은경우에는기본값을사용해서200을응답한다() throws Exception {
      int page = 1;
      int pageSize = 10;

      String url = new StringBuilder("/api/v1/post")
              .toString();

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

      String url = new StringBuilder("/api/v1/post")
              .append("?page=" + page)
              .append("&size=" + pageSize)
              .toString();

      when(postService.getPostList(PageRequest.of(page - 1, pageSize)))
              .thenReturn(createDummy(pageSize, TOTAL_COUNT));

      mockMvc.perform(get(url))
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
