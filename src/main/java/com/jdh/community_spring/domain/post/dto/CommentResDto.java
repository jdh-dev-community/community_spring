package com.jdh.community_spring.domain.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CommentResDto {
  @Schema(description = "댓글의 id", example = "1")
  private long commentId;

  @Schema(description = "댓글의 내용", example = "Why this error occurs?")
  private String content;

  @Schema(description = "댓글의 작성자", example = "jack")
  private String creator;

  @Schema(description = "댓글의 생성일자", example = "2023-01-01T12:00:00")
  private LocalDateTime createdAt;

  @Schema(description = "대댓글 목록", example = "대댓글[]")
  private List<CommentResDto> children = new ArrayList<>();

}
