package com.jdh.community_spring.domain.post.service.impls;


import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.exception.NotFoundException;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.CommentDto;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.dto.CommentResDto;
import com.jdh.community_spring.domain.post.repository.CommentRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;

  private final PostRepository postRepository;

  private final SimpleEncrypt simpleEncrypt;


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
      Comment comment = createComment(dto, parentComment);
      Comment savedComment = commentRepository.save(comment);
      return createCommentResDto(savedComment);
    } catch (Exception ex) {
      log.error("입력값: {}, 메세지: {}", dto, ex.getMessage());
      throw ex;
    }
  }


  private Comment createComment(CommentCreateReqDto dto, Comment parent) {
    String hashedPassword = simpleEncrypt.encrypt(dto.getPassword());
    return Comment.builder()
            .content(dto.getContent())
            .creator(dto.getCreator())
            .password(hashedPassword)
            .parentComment(parent)
            .build();
  }

  private CommentResDto createCommentResDto(Comment comment) {
    List<CommentResDto> recomments = comment.getChildComments().stream().map(this::createReCommentResDto).collect(Collectors.toList());

    return CommentResDto.builder()
            .commentId(comment.getCommentId())
            .content(comment.getContent())
            .creator(comment.getCreator())
            .createdAt(comment.getCreatedAt())
            .children(recomments)
            .build();
  }

  private CommentResDto createReCommentResDto(Comment comment) {
    return CommentResDto.builder()
            .commentId(comment.getCommentId())
            .content(comment.getContent())
            .creator(comment.getCreator())
            .createdAt(comment.getCreatedAt())
            .build();
  }
}
