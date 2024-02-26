package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.*;

import java.util.List;

public interface CommentService {
  CommentDto createComment(long postId, CommentCreateReqDto dto);
  ListResDto<CommentDto> getCommentList(long postId, ListReqDto dto);
  List<CommentDto> getChildCommentList(long commentId, ListReqDto dto);
  TokenResDto generateToken(long commentId, TokenReqDto dto);
  CommentDto deleteComment(long commentId);

}
