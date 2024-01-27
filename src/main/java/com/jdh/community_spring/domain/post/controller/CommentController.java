package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.dto.CommentResDto;
import com.jdh.community_spring.domain.post.service.interfaces.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "게시글 생성", description = "제목, 내용, 작성자, 카테고리를 포함하는 게시글을 작성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post/{id}/comment")
  public CommentResDto createComment(
          @PathVariable String id,
          @Valid @RequestBody CommentCreateReqDto dto) {

    long postId = Long.parseLong(id);
    CommentResDto result = commentService.createComment(postId, dto);

    return result;
  }
}
