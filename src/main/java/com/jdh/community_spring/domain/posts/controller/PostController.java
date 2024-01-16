package com.jdh.community_spring.domain.posts.controller;

import com.jdh.community_spring.domain.posts.dto.PostCreateDto;
import com.jdh.community_spring.domain.posts.service.interfaces.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post")
  public void createPost(@RequestBody PostCreateDto dto) {
    postService.createPost(dto);
  }



}
