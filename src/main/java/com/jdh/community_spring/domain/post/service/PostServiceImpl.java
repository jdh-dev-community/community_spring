package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.constant.OrderBy;
import com.jdh.community_spring.common.constant.SortBy;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.exception.NotFoundException;
import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.*;
import com.jdh.community_spring.domain.post.repository.CommentRepository;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.interfaces.CommentService;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  private final InMemoryDBProvider inMemoryDBProvider;

  private final CommentService commentService;

  private final SimpleEncrypt simpleEncrypt;

  @Override
  public PostResDto createPost(PostCreateReqDto dto) {
    try {
      Post post = createPostEntity(dto);
      Post savedPost = postRepository.save(post);
      return createPostResDto(savedPost);
    } catch (Exception ex) {
      log.error("입력값: {}, 메세지: {}", dto, ex.getMessage());
      throw ex;
    }
  }


  private Post createPostEntity(PostCreateReqDto dto) {

    String hashedPassword = simpleEncrypt.encrypt(dto.getPassword());
    return Post.builder()
            .title(dto.getTitle())
            .textContent(dto.getContent())
            .creator(dto.getCreator())
            .category(dto.getCategory().getCategory())
            .viewCount(0)
            .password(hashedPassword)
            .build();
  }

  @Override
  public ListResDto<PostCommentCountDto> getPostList(ListReqDto listReqDto) {
    Pageable pageable = listReqDto.toPageable();
    Page<PostCommentCountDto> post = postRepository.findAllPostWithCommentCount(pageable);

    return new ListResDto<>(post.getTotalElements(), post.getContent());
  }

  private PostResDto createPostResDto(Post post) {
    List<Comment> commentList = post.getComments() != null ? post.getComments() : new ArrayList<>();

    List<CommentResDto> comments = commentList
            .stream()
            .map(this::createComment).collect(Collectors.toList());


    return PostResDto.builder()
            .postId(post.getPostId())
            .title(post.getTitle())
            .content(post.getTextContent())
            .category(post.getCategory())
            .creator(post.getCreator())
            .viewCount(post.getViewCount())
            .comments(comments)
            .createdAt(post.getCreatedAt())
            .build();
  }

  private CommentResDto createComment(Comment comment) {
    List<CommentResDto> children = comment.getChildComments()
            .stream()
            .map(this::createReComment).collect(Collectors.toList());

    return CommentResDto.builder().commentId(comment.getCommentId())
            .content(comment.getContent())
            .creator(comment.getCreator())
            .createdAt(comment.getCreatedAt())
            .children(children)
            .build();
  }

  private CommentResDto createReComment(Comment comment) {
    return CommentResDto.builder()
            .commentId(comment.getCommentId())
            .content(comment.getContent())
            .creator(comment.getCreator())
            .createdAt(comment.getCreatedAt())
            .build();
  }

  @Transactional
  @Override
  public PostCommentsDto getPost(long postId) {
    Post post = postRepository.findByIdInPessimisticWrite(postId)
            .orElseThrow(() -> new EntityNotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다"));

    post.setViewCount(post.getViewCount() + 1);
    postRepository.save(post);

    ListReqDto dto = ListReqDto.of(1, 3, SortBy.RECENT, OrderBy.DESC);
    List<CommentChildrenCountDto> comments = commentService.getCommentList(postId, dto);

    return PostCommentsDto.of(post, comments);
  }

  @Override
  public PostTokenResDto generateToken(PostTokenReqDto dto) {

    Optional<Post> optPost = postRepository.findById(dto.getPostId());
    Post post = optPost.orElseThrow(() -> new NotFoundException("[postId: " + dto.getPostId() + "] 게시글이 존재하지 않습니다"));
    boolean isValidPassword = simpleEncrypt.match(dto.getPassword(), post.getPassword());

    if (isValidPassword) {
      String token = simpleEncrypt.encrypt(dto.getPostId() + dto.getPassword());
      inMemoryDBProvider.setTemperarily(String.valueOf(dto.getPostId()), token, 3 * 60);
      return new PostTokenResDto(token);
    } else {
      throw new IllegalArgumentException("잘못된 비밀번호입니다. 비밀번호를 확인해주세요");
    }
  }

  @Override
  public void deletePost(String id) {
    postRepository.deleteById(Long.parseLong(id));
  }

  @Override
  public PostResDto editPost(String id, PostEditReqDto dto) {
    long postId = Long.parseLong(id);
    Optional<Post> optPost = postRepository.findById(postId);
    Post post = optPost.orElseThrow(() -> new NotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다"));

    // TODO: mapper 사용하기
    post.setTitle(dto.getTitle());
    post.setTextContent(dto.getContent());
    post.setCategory(dto.getCategory());
    post.setCreator(dto.getCreator());

    // TODO: save와 dirty check 쿼리 비교하기
    postRepository.save(post);

    PostResDto result = createPostResDto(post);
    return result;
  }
}




