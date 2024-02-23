package com.jdh.community_spring.domain.post.service.impls;


import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.CommentDto;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.repository.CommentRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
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

  @Override
  public CommentDto createComment(long postId, CommentCreateReqDto dto) {
    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다"));

    Comment parentComment = dto.getParentId() != null
            ? commentRepository.findById(dto.getParentId())
            .orElseThrow(() -> new EntityNotFoundException("[commentId: " + dto.getParentId() + "] 댓글이 존재하지 않습니다"))
            : null;

    Comment comment = Comment.builder()
            .content(dto.getContent())
            .creator(dto.getCreator())
            .password(simpleEncrypt.encrypt(dto.getPassword()))
            .parentComment(parentComment)
            .post(post)
            .build();

    commentRepository.save(comment);

    return CommentDto.from(comment);
  }
}
