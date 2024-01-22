package com.jdh.community_spring.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@ToString
@RequiredArgsConstructor
public class CreateResDto {

  @Schema(description = "게시글의 id", example = "1")
  @NotNull(message = "게시글 id는 필수 입력 값입니다.")
  private final long postId;

  @Schema(description = "게시글의 제목", example = "Why this error occurs?")
  @NotBlank(message = "제목은 필수 입력 값입니다.")
  private final String title;

  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  @NotBlank(message = "내용은 필수 입력 값입니다.")
  private final String content;

  @Schema(description = "게시글의 카테고리", example = "질문, 홍보, 상담")
  @NotNull(message = "카테고리는 필수 입력 값입니다.")
  private final String category;

  @Schema(description = "게시글의 작성자", example = "jack")
  @NotNull(message = "작성자는 필수 입력 값입니다.")
  private final String creator;

  @Schema(description = "게시글의 조회수", example = "100")
  @NotNull(message = "조회수는 필수 입력 값입니다.")
  private final long viewCount;

  @Schema(description = "게시글의 생성일자", example = "2023-01-01T12:00:00")
  private final LocalDateTime createdAt;
}
