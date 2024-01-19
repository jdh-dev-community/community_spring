package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {

  private final PostService postService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post")
  public void createPost(@Valid @RequestBody CreateReqDto dto) {
    postService.createPost(dto);
  }

  @GetMapping("/post")
  public ListReqDto<Post> getPostList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size);
    ListReqDto<Post> dto = postService.getPostList(pageable);

    return dto;
  }
}
