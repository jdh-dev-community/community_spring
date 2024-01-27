package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.*;

public interface PostService {
  PostResDto createPost(PostCreateReqDto dto);
  ListResDto<PostResDto> getPostList(ListReqDto listReqDto);
  PostResDto getPost(String id);
  PostTokenResDto generateToken(PostTokenReqDto dto);
  void deletePost(String id);
  PostResDto editPost(String id, PostEditReqDto dto);
}
