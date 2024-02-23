package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CommentChildrenCountDto;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.dto.CommentResDto;

import java.util.List;

public interface CommentService {
  CommentResDto createComment(long postId, CommentCreateReqDto dto);
  List<CommentChildrenCountDto> getCommentList(long postId, ListReqDto dto);
}
