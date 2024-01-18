package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.PostCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
  void createPost (PostCreateDto dto);

  ListReqDto<Post> getPostList(Pageable pageable);
}
