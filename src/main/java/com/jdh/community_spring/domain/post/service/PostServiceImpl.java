package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.exception.NotFoundException;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  private final PostMapper postMapper;


  @Override
  public void createPost(CreateReqDto dto) {
    try {
      Post post = postMapper.toEntity(dto);
      postRepository.save(post);
    } catch (Exception ex) {
      log.error("입력값: {}, 메세지: {}",dto, ex.getMessage());
      throw ex;
    }
  }

  @Override
  public ListResDto<Post> getPostList(Pageable pageable) {
    if (pageable == null) throw new IllegalArgumentException("Pageable이 존재하지 않습니다.");

    // TODO: post를 CreateResDto로 변경해서 ListReqDto에 담기
    Page<Post> page = postRepository.findAll(pageable);
    ListResDto<Post> dto = new ListResDto<>(page.getTotalElements(), page.getContent());
    return dto;
  }

  @Override
  public PostResDto getPost(String id) {
    long postId = Long.parseLong(id);
    Optional<Post> optPost = postRepository.findById(postId);
    Post post = optPost.orElseThrow(() -> new NotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다"));
    PostResDto result = postMapper.toPostResDto(post);

    return result;
  }
}




