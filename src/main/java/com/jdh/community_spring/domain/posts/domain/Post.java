package com.jdh.community_spring.domain.posts.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private long postId;

  @Column(name = "title")
  private String title;

  @Column(name = "text_content")
  private String textContent;


  // TODO: 유저 테이블 생성 후 맵핑
  @Column(name = "creator")
  private String creator;


  // TODO: 카테고리 정리 후 테이블로 생성
  @Column(name = "category")
  private String category;

  @Column(name = "view_count")
  private int viewCount;

  @Column(name = "created_at")
  private LocalDateTime createdAt;


  public Post (String title, String textContent, String creator, String category) {
    title = title;
    textContent = textContent;
    creator = creator;
    category = category;
  }

}
