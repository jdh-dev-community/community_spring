package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.dto.CommentChildrenCountDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CustomCommentRepository {
  List<CommentChildrenCountDto> findCommentsByPostId(long postId, Pageable pageable);
}
