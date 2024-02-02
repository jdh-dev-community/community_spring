package com.jdh.community_spring.domain.post.controller;


import com.jdh.community_spring.domain.post.dto.PostResDto;
import com.jdh.community_spring.domain.post.service.PostServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/test")
public class PostTestController {

  private final PostServiceImpl postService;

  @Operation(summary = "게시글 상세", description = "게시글 id를 기반으로 상세 내용을 불러오는 api 입니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/post/{id}")
  public PostResDto getPost(@PathVariable String id) {

    long postId = Long.parseLong(id);
    PostResDto post = postService.getPost_Test(postId);

    return post;
  }
}
