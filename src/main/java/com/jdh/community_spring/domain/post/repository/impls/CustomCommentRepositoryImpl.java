package com.jdh.community_spring.domain.post.repository.impls;

import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.dto.CommentDto;
import com.jdh.community_spring.domain.post.repository.CustomBaseRepository;
import com.jdh.community_spring.domain.post.repository.CustomCommentRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import com.jdh.community_spring.domain.post.domain.QComment;
import static com.jdh.community_spring.domain.post.domain.QComment.comment;

@Slf4j
public class CustomCommentRepositoryImpl implements CustomCommentRepository, CustomBaseRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final PathBuilder<Comment> entityPath;
  public CustomCommentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
    this.entityPath = new PathBuilder<>(Comment.class, "comment");
  }

  @Override
  public List<CommentDto> findCommentsByPostId(long postId, Pageable pageable) {
    QComment commentAlias = new QComment("commentAlias");

    List<Tuple> comments = jpaQueryFactory
            .select(
                    comment.commentId,
                    comment.content,
                    comment.creator,
                    comment.createdAt,
                    commentAlias.commentId.count()
            ).from(comment)
            .leftJoin(commentAlias)
            .on(commentAlias.parentComment.commentId.eq(comment.commentId))
            .where(
                    comment.post.postId.eq(postId),
                    comment.parentComment.isNull()
            )
            .groupBy(comment.commentId)
            .orderBy(extractOrder(pageable.getSort(), entityPath))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    List<CommentDto> dtos = comments.stream()
            .map((result) -> CommentDto.of(
                    result.get(comment.commentId),
                    result.get(comment.content),
                    result.get(comment.creator),
                    result.get(comment.createdAt),
                    result.get(commentAlias.commentId.count()),
                    postId
            )).collect(Collectors.toList());

    return dtos;
  }
}
