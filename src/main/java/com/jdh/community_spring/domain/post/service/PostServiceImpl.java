package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
  public ListReqDto<Post> getPostList(Pageable pageable) {
    if (pageable == null) throw new IllegalArgumentException("Pageable이 존재하지 않습니다.");

    // TODO: post를 CreateResDto로 변경해서 ListReqDto에 담기
    Page<Post> page = postRepository.findAll(pageable);
    ListReqDto<Post> dto = new ListReqDto<>(page.getTotalElements(), page.getContent());
    return dto;
  }

}
