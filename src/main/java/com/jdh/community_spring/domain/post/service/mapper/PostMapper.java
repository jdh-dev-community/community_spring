package com.jdh.community_spring.domain.post.service.mapper;

import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
  PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

  @Mapping(source = "content", target = "textContent")
  Post toEntity(CreateReqDto dto, @Context SimpleEncrypt simpleEncrypt);

  @AfterMapping
  default void customMapping(@MappingTarget Post post, CreateReqDto dto, @Context SimpleEncrypt simpleEncrypt) {
    String hashedPassword = simpleEncrypt.encrypt(dto.getPassword());
    post.setPassword(hashedPassword);
  }

  @Mapping(source = "textContent", target = "content")
  PostResDto toPostResDto(Post entity);
}
