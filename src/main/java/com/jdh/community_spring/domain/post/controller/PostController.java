package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {

  private final PostService postService;

  @Operation(summary = "게시글 생성", description = "제목, 내용, 작성자, 카테고리를 포함하는 게시글을 작성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post")
  public void createPost(@Valid @RequestBody CreateReqDto dto) {
    postService.createPost(dto);
  }



  @Operation(summary = "게시글 목록", description = "게시글 목록을 페이지별로 불러올 수 있는 api 입니다.")
  @GetMapping("/post")
  public ListReqDto<Post> getPostList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size);
    ListReqDto<Post> dto = postService.getPostList(pageable);

    return dto;
  }


  @Operation(summary = "게시글 상세", description = "게시글 id를 기반으로 상세 내용을 불러오는 api 입니다.")
  @GetMapping("/post/{id}")
  public PostResDto getPost(@PathVariable String id) {
    PostResDto post = postService.getPost(id);

    return post;
  }
}
