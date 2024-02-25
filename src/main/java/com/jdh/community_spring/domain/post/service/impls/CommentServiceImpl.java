package com.jdh.community_spring.domain.post.service.impls;


import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.domain.mapper.CommentMapper;
import com.jdh.community_spring.domain.post.dto.*;
import com.jdh.community_spring.domain.post.repository.CommentRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final PostRepository postRepository;

  private final CommentMapper commentMapper;

  private final SimpleEncrypt simpleEncrypt;

  private final InMemoryDBProvider inMemoryDBProvider;


  public List<CommentDto> getChildCommentList(long commentId, ListReqDto dto) {
    Pageable pageable = dto.toPageable();
    Page<Comment> comments = commentRepository.findAllByParentCommentId(commentId, pageable);
    List<CommentDto> commentDtos = comments.stream().map(CommentDto::from).collect(Collectors.toList());

    return commentDtos;
  }

  public List<CommentDto> getCommentList(long postId, ListReqDto dto) {
    Pageable pageable = dto.toPageable();
    List<CommentDto> comments = commentRepository.findCommentsByPostId(postId, pageable);

    return comments;
  }

  @Override
  public CommentDto createComment(long postId, CommentCreateReqDto dto) {
    try {
      Post post = postRepository.findByIdWithException(postId);

      Comment parentComment = dto.getParentId() != null
              ? commentRepository.findByIdWithException(dto.getParentId())
              : null;

      Comment comment = commentMapper.of(dto, post, parentComment);
      commentRepository.save(comment);

      return CommentDto.from(comment);
    } catch (Exception ex) {
      log.error("입력값: {}, 메세지: {}", dto, ex.getMessage());
      throw ex;
    }
  }

  @Override
  public TokenResDto generateToken(long commentId, TokenReqDto dto) {
    Comment comment = commentRepository.findByIdWithException(commentId);
    boolean isValidPassword = simpleEncrypt.match(dto.getPassword(), comment.getPassword());

    if (isValidPassword) {
      String token = simpleEncrypt.encrypt(commentId + dto.getPassword());
      inMemoryDBProvider.setTemperarily(String.valueOf(commentId), token, 3 * 60);
      return new TokenResDto(token);
    } else {
      throw new IllegalArgumentException("잘못된 비밀번호입니다. 비밀번호를 확인해주세요");
    }
  }

  @Override
  public void deleteComment(long commentId) {
    commentRepository.deleteById(commentId);
  }


}
