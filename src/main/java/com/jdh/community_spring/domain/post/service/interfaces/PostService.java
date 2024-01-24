package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import org.springframework.data.domain.Pageable;

public interface PostService {
  void createPost (CreateReqDto dto);
  ListResDto<Post> getPostList(Pageable pageable);
  PostResDto getPost(String id);
}
