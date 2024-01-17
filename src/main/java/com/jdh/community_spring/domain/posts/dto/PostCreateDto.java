package com.jdh.community_spring.domain.posts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
public class PostCreateDto {
  private long postId;
  private String title;
  private String category;
  private String creator;
  private String content;
  private int viewCount;
  private LocalDateTime createdAt;
}
