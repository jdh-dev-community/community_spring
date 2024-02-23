package com.jdh.community_spring.domain.post.repository;


import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.PostCommentCountDto;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.jdh.community_spring.domain.post.domain.QPost.post;
import static com.jdh.community_spring.domain.post.domain.QComment.comment;

@Slf4j
@RequiredArgsConstructor
@Repository
public class CustomPostRepositoryImpl implements CustomPostRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<PostCommentCountDto> findAllPostWithCommentCount(Pageable pageable) {
    long count = jpaQueryFactory
            .select(post.count())
            .from(post)
            .fetchOne();

    List<Tuple> results = jpaQueryFactory
            .select(
                    post.postId,
                    post.title,
                    post.textContent,
                    post.category,
                    post.creator,
                    post.viewCount,
                    comment.commentId.count(),
                    post.createdAt)
            .from(post)
            .leftJoin(comment).on(comment.post.postId.eq(post.postId))
            .groupBy(post.postId)
            .orderBy(extractOrder(pageable.getSort()))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

    List<PostCommentCountDto> dtos = results.stream()
            .map((result) -> PostCommentCountDto.of(
                    result.get(post.postId),
                    result.get(post.title),
                    result.get(post.textContent),
                    result.get(post.category),
                    result.get(post.creator),
                    result.get(post.viewCount),
                    result.get(comment.commentId.count()),
                    result.get(post.createdAt)
            )).collect(Collectors.toList());


    return new PageImpl<>(dtos, pageable, count);
  }

  private OrderSpecifier[] extractOrder(Sort sort) {
    PathBuilder<Post> entityPath = new PathBuilder<>(Post.class, "post");
    List<OrderSpecifier> orders = new ArrayList<>();

    if (sort.isSorted()) {
      for (Sort.Order order : sort) {
        PathBuilder<Object> path = entityPath.get(order.getProperty());
        Order selectedOrder = order.isAscending() ? Order.ASC : Order.DESC;

        orders.add(new OrderSpecifier(selectedOrder, path));
      }
    }

    return orders.toArray(new OrderSpecifier[0]);
  }

}
