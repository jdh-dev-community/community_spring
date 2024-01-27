package com.jdh.community_spring.domain.post.service.interfaces;

import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.dto.CommentResDto;

public interface CommentService {
  CommentResDto createComment(long postId, CommentCreateReqDto dto);
}
