package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CommentDto;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;

import java.util.List;

public interface CommentService {
  CommentDto createComment(long postId, CommentCreateReqDto dto);
  List<CommentDto> getCommentList(long postId, ListReqDto dto);
  List<CommentDto> getChildCommentList(long commentId, ListReqDto dto);

}
