package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.PostCreateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface PostMapper {
  PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);


  @Mapping(source = "content", target = "textContent")
  Post toEntity(PostCreateDto dto);

  @Mapping(source = "textContent", target = "content")
  PostCreateDto toPostCreateDto(Post entity);

}
