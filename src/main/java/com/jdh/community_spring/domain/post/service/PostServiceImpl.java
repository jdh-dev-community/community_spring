package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.exception.InvalidInputException;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.PostCreateDto;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  private final PostMapper postMapper;

  @Override
  public void createPost(PostCreateDto dto) {
    Post post = postMapper.toEntity(dto);
    postRepository.save(post);
  }


  @Override
  public ListReqDto<Post> getPostList(Pageable pageable) {
    if (pageable == null) throw new InvalidInputException("Pageable이 존재하지 않습니다.");

    Page<Post> page = postRepository.findAll(pageable);
    ListReqDto<Post> dto = new ListReqDto<>(page.getTotalElements(), page.getContent());
    return dto;
  }

}
