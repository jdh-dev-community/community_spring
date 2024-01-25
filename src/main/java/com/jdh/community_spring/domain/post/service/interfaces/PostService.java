package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostAuthReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;

public interface PostService {
  void createPost (CreateReqDto dto);
  ListResDto<PostResDto> getPostList(ListReqDto listReqDto);
  PostResDto getPost(String id);
  String generateToken(PostAuthReqDto dto);
}
