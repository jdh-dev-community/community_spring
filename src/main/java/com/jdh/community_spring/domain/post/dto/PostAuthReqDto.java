package com.jdh.community_spring.domain.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@RequiredArgsConstructor
public class PostAuthReqDto {
  @Schema(description = "게시글의 id", example = "1")
  @NotBlank(message = "id는 필수값입니다.")
  private final long postId;

  @Schema(description = "게시글의 비밀번호", example = "1234")
  @NotBlank(message = "비밀번호는 필수값입니다.")
  private final String password;
}
