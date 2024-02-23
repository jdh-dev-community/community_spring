package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CommentDto;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "댓글 생성", description = "댓글, 작성자를 포함하는 댓글을 작성합니다.")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post/{id}/comment")
  public CommentDto createComment(
          @PathVariable long id,
          @Valid @RequestBody CommentCreateReqDto dto
  ) {
    CommentDto result = commentService.createComment(id, dto);

    return result;
  }

  @Operation(summary = "댓글 목록 조회", description = "최상위 댓글 목록을 조회합니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/post/{id}/comment")
  public List<CommentDto> getCommentList(
          @PathVariable("id") long postId,
          @Valid @ModelAttribute ListReqDto dto
  ) {
    List<CommentDto> comments = commentService.getCommentList(postId, dto);
    return comments;
  }

  @Operation(summary = "대댓글 목록 조회", description = "대댓글 목록을 조회합니다.")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/post/{id}/comment/{commentId}")
  public List<CommentDto> getChildComment(
          @PathVariable long commentId,
          @Valid @ModelAttribute ListReqDto dto
  ) {
    List<CommentDto> comments = commentService.getChildCommentList(commentId, dto);
    return comments;
  }

}
