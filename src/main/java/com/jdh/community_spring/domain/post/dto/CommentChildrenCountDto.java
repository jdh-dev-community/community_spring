package com.jdh.community_spring.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentChildrenCountDto {
  @Schema(description = "댓글의 id", example = "1")
  private long commentId;

  @Schema(description = "댓글의 내용", example = "Why this error occurs?")
  private String content;

  @Schema(description = "댓글의 작성자", example = "jack")
  private String creator;

  @Schema(description = "댓글의 생성일자", example = "2023-01-01T12:00:00")
  private LocalDateTime createdAt;

  @Schema(description = "대댓글의 숫자", example = "0")
  private long childrenCommentCount;

  public static CommentChildrenCountDto of(long commentId, String content, String creator, LocalDateTime createdAt, long childrenCommentCount) {
    return CommentChildrenCountDto.builder()
            .commentId(commentId)
            .content(content)
            .creator(creator)
            .createdAt(createdAt)
            .childrenCommentCount(childrenCommentCount)
            .build();
  }


}
