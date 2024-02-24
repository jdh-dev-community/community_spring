package com.jdh.community_spring.domain.post.domain.mapper;

import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.PostCreateReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
  private final SimpleEncrypt simpleEncrypt;

  public Post from(PostCreateReqDto dto) {
    return Post.builder()
            .title(dto.getTitle())
            .textContent(dto.getContent())
            .creator(dto.getCreator())
            .category(dto.getCategory())
            .viewCount(0)
            .password(simpleEncrypt.encrypt(dto.getPassword()))
            .build();
  }
}
