package com.jdh.community_spring.domain.post.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@ToString
@RequiredArgsConstructor
public class CreateReqDto {

  @NotBlank(message = "제목은 필수 입력 값입니다.")
  private final String title;

  @NotBlank(message = "내용은 필수 입력 값입니다.")
  private final String content;

  @NotNull(message = "카테고리는 필수 입력 값입니다.")
  private final String category;

  @NotNull(message = "작성자는 필수 입력 값입니다.")
  private final String creator;

}
