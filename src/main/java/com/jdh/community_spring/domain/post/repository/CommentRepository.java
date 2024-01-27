package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
