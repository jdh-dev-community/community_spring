package com.jdh.community_spring.domain.post.domain;

import com.jdh.community_spring.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post extends BaseEntity {

  @Schema(description = "id", example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private long postId;

  @Schema(description = "게시글의 제목", example = "Why this error occurs?")
  @Column(name = "title")
  private String title;

  @Schema(description = "게시글의 내용", example = "When I start my server, the error below shows")
  @Column(name = "text_content")
  private String textContent;


  @Schema(description = "게시글의 작성자", example = "jack")
  // TODO: 유저 테이블 생성 후 맵핑
  @Column(name = "creator")
  private String creator;


  @Schema(description = "게시글의 카테고리", example = "질문, 홍보, 상담")
  // TODO: 카테고리 정리 후 테이블로 생성
  @Column(name = "category")
  private String category;

  @Schema(description = "게시글의 조회수", example = "100")
  @Column(name = "view_count")
  private int viewCount;

  public Post (String title, String textContent, String creator, String category) {
    this.title = title;
    this.textContent = textContent;
    this.creator = creator;
    this.category = category;
  }

}
