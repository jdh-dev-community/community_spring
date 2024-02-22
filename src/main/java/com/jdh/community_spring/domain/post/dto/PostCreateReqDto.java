package com.jdh.community_spring.domain.post.dto;

import com.jdh.community_spring.common.constant.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@ToString
@NoArgsConstructor
public class PostCreateReqDto {
  @Schema(description = "게시글의 제목", example = "Why this error occurs?")
  @NotBlank(message = "제목은 필수 입력 값입니다.")
  private  String title;

  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  @NotBlank(message = "내용은 필수 입력 값입니다.")
  private  String content;

  @Schema(description = "게시글의 카테고리", example = "질문, 홍보, 상담")
  @NotNull(message = "카테고리는 필수 입력 값입니다.")
  private  PostCategory category;

  @Schema(description = "게시글의 작성자", example = "jack")
  @NotNull(message = "작성자는 필수 입력 값입니다.")
  private  String creator;

  @Schema(description = "게시글 비밀번호", example = "1234")
  @NotNull(message = "비밀번호는 필수 입력값입니다.")
  @Size(min = 4)
  private  String password;

  public void setCategory(String category) {
    this.category = PostCategory.match(category);
  }

}
