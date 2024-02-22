package com.jdh.community_spring.domain.post.domain;

import com.jdh.community_spring.common.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.Fetch;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

  @Schema(description = "댓글 id" , example = "1")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private long commentId;

  @Schema(description = "댓글의 내용", example = "I like ur comment")
  @Column(name = "content", nullable = false, columnDefinition = "TEXT" )
  private String content;

  @Schema(description = "댓글의 작성자", example = "david")
  // TODO: 유저 테이블 생성 후 맵핑
  @Column(name = "creator", nullable = false)
  private String creator;

  @Schema(description = "최소 4자리를 사용하는 게시글의 비밀번호", example = "1234")
  @Column(name = "password", nullable = false)
  private String password;

  @Schema(description = "부모 댓글 정보")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Comment parentComment;

  @Schema(description = "자식 댓글")
  @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> childComments = new ArrayList<>();


  @Schema(description = "댓글이 작성된 게시글 정보")
  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

}
