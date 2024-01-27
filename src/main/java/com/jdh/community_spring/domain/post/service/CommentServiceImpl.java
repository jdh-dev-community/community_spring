package com.jdh.community_spring.domain.post.service;


import com.jdh.community_spring.common.exception.NotFoundException;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.dto.CommentResDto;
import com.jdh.community_spring.domain.post.repository.CommentRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.interfaces.CommentService;
import com.jdh.community_spring.domain.post.service.mapper.CommentMapper;
import com.jdh.community_spring.domain.post.service.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final PostRepository postRepository;

  private final CommentMapper commentMapper;

  private final SimpleEncrypt simpleEncrypt;


  @Transactional
  @Override
  public CommentResDto createComment(long postId, CommentCreateReqDto dto) {
    Optional<Post> optPost = postRepository.findById(postId);
    Post post = optPost.orElseThrow(() -> new NotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다"));

    Comment parentComment = null;
    if (dto.getParentId() != null) {
      Optional<Comment> optComment = commentRepository.findById(dto.getParentId());
      parentComment = optComment.orElseThrow(() -> new NotFoundException("[commentId: " + dto.getParentId() + "] 댓글이 존재하지 않습니다"));
    }

    try {
      Comment comment = commentMapper.toEntity(dto, simpleEncrypt, post, parentComment);
      Comment savedComment = commentRepository.save(comment);
      return commentMapper.toCommentResDto(savedComment);
    } catch (Exception ex) {
      log.error("입력값: {}, 메세지: {}", dto, ex.getMessage());
      throw ex;
    }
  }
}
