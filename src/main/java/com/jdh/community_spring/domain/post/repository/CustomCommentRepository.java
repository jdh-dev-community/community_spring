package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.dto.CommentDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCommentRepository {
  List<CommentDto> findCommentsByPostId(long postId, Pageable pageable);
}
