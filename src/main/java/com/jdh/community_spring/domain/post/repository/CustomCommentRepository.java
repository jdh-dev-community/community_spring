package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.dto.CommentChildrenCommentCountDto;

import java.util.List;

public interface CustomCommentRepository {
  List<CommentChildrenCommentCountDto> findCommentsByPostId(long postId);
}
