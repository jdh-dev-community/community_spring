package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.common.exception.NotFoundException;
import com.jdh.community_spring.common.provider.InMemoryDBProvider;
import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListResDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostAuthReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import com.jdh.community_spring.domain.post.service.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;

  private final PostMapper postMapper;

  private final InMemoryDBProvider inMemoryDBProvider;

  private final SimpleEncrypt simpleEncrypt;


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
  public ListResDto<PostResDto> getPostList(ListReqDto listReqDto) {
    Pageable pageable = listReqDto.toPageable();

    Page<Post> page = postRepository.findAll(pageable);
    List<PostResDto> dto = page.getContent().stream()
            .map(postMapper::toPostResDto)
            .collect(Collectors.toList());

    return new ListResDto<>(page.getTotalElements(), dto);
  }

  @Override
  public PostResDto getPost(String id) {
    long postId = Long.parseLong(id);
    Optional<Post> optPost = postRepository.findById(postId);
    Post post = optPost.orElseThrow(() -> new NotFoundException("[postId: " + postId + "] 게시글이 존재하지 않습니다"));
    PostResDto result = postMapper.toPostResDto(post);

    return result;
  }

  @Override
  public String generateToken(PostAuthReqDto dto) {
    Optional<Post> optPost = postRepository.findById(dto.getPostId());
    Post post = optPost.orElseThrow(() -> new NotFoundException("[postId: " + dto.getPostId() + "] 게시글이 존재하지 않습니다"));
    boolean isValidPassword = simpleEncrypt.match(dto.getPassword(), post.getPassword());

    if (isValidPassword) {
      String token = simpleEncrypt.encrypt(dto.getPostId() + dto.getPassword());
      inMemoryDBProvider.setTemperarily(String.valueOf(dto.getPostId()), token, 3 * 60);
      return token;
    } else {
      throw new IllegalArgumentException("잘못된 비밀번호입니다. 비밀번호를 확인해주세요");
    }
  }
}




