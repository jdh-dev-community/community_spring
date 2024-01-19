package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import org.springframework.data.domain.Pageable;

public interface PostService {
  void createPost (CreateReqDto dto);

  ListReqDto<Post> getPostList(Pageable pageable);
}
