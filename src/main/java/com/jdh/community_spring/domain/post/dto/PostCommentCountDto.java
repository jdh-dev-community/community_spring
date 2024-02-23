package com.jdh.community_spring.domain.post.dto;

import com.jdh.community_spring.common.constant.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostCommentCountDto {
  @Schema(description = "게시글의 id", example = "1")
  private long postId;

  @Schema(description = "게시글의 제목", example = "Why this error occurs?")
  private String title;

  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  private String content;

  @Schema(description = "게시글의 카테고리 (question, ad, consulting)", example = "question")
  private PostCategory category;

  @Schema(description = "게시글의 작성자", example = "jack")
  private String creator;

  @Schema(description = "게시글의 조회수", example = "100")
  @NotNull(message = "조회수는 필수 입력 값입니다.")
  private long viewCount;

  @Schema(description = "댓글 수", example = "100")
  private long commentCount;

  @Schema(description = "게시글의 생성일자", example = "2023-01-01T12:00:00")
  private LocalDateTime createdAt;

  @Builder
  public PostCommentCountDto(long postId, String title, String content, String category, String creator, long viewCount, long commentCount, LocalDateTime createdAt) {
    this.postId = postId;
    this.title = title;
    this.content = content;
    this.category = PostCategory.match(category);
    this.creator = creator;
    this.viewCount = viewCount;
    this.commentCount = commentCount;
    this.createdAt = createdAt;
  }
}
