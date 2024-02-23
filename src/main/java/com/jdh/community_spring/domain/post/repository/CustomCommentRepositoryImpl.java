package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.dto.CommentChildrenCommentCountDto;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import com.jdh.community_spring.domain.post.domain.QComment;
import static com.jdh.community_spring.domain.post.domain.QComment.comment;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CustomCommentRepositoryImpl implements CustomCommentRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<CommentChildrenCommentCountDto> findCommentsByPostId(long postId) {
    QComment commentAlias = new QComment("commentAlias");
    int INIT_COMMENT_COUNT = 5;

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
            .orderBy(comment.createdAt.desc())
            .limit(INIT_COMMENT_COUNT)
            .fetch();

    List<CommentChildrenCommentCountDto> dtos = comments.stream()
            .map((result) -> CommentChildrenCommentCountDto.of(
                    result.get(comment.commentId),
                    result.get(comment.content),
                    result.get(comment.creator),
                    result.get(comment.createdAt),
                    result.get(commentAlias.commentId.count())
            )).collect(Collectors.toList());

    return dtos;
  }

}
