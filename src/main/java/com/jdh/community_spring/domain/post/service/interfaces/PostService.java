package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostTokenReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import com.jdh.community_spring.domain.post.dto.PostTokenResDto;

public interface PostService {
  void createPost (CreateReqDto dto);
  ListResDto<PostResDto> getPostList(ListReqDto listReqDto);
  PostResDto getPost(String id);
  PostTokenResDto generateToken(PostTokenReqDto dto);
}
