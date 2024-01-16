package com.jdh.community_spring.domain.posts.repository;

import com.jdh.community_spring.domain.posts.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
