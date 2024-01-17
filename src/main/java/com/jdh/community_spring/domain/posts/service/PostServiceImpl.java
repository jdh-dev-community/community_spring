package com.jdh.community_spring.domain.posts.service;

import com.jdh.community_spring.domain.posts.domain.Post;
import com.jdh.community_spring.domain.posts.dto.PostCreateDto;
import com.jdh.community_spring.domain.posts.repository.PostRepository;
import com.jdh.community_spring.domain.posts.service.interfaces.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  private final PostMapper postMapper;

  @Override
  public void createPost (PostCreateDto dto) {
    Post post = postMapper.toEntity(dto);
    postRepository.save(post);
  }

}
