package com.jdh.community_spring.domain.posts.service.interfaces;

import com.jdh.community_spring.domain.posts.dto.PostCreateDto;

public interface PostService {
  void createPost (PostCreateDto dto);
}
