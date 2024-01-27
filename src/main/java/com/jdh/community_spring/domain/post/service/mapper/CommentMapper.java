package com.jdh.community_spring.domain.post.service.mapper;

import com.jdh.community_spring.common.util.SimpleEncrypt;
import com.jdh.community_spring.domain.post.domain.Comment;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.CommentCreateReqDto;
import com.jdh.community_spring.domain.post.dto.CommentResDto;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
  CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

  @Mapping(source = "content", target = "content")
  Comment toEntity(CommentCreateReqDto dto, @Context SimpleEncrypt simpleEncrypt, @Context Post post, @Context Comment parentComment);

  @AfterMapping
  default void customMapping(@MappingTarget Comment comment, CommentCreateReqDto dto, @Context SimpleEncrypt simpleEncrypt, @Context Post post, @Context Comment parentComment) {
    String hashedPassword = simpleEncrypt.encrypt(dto.getPassword());
    comment.setPassword(hashedPassword);
    comment.setPost(post);
    if (parentComment != null) comment.setParentComment(parentComment);
  }

  @Mapping(source = "content", target = "content")
  CommentResDto toCommentResDto(Comment entity);

  @AfterMapping
  default void customMapping(@MappingTarget CommentResDto result, Comment entity) {
    Long parentId = entity.getParentComment() != null? entity.getParentComment().getCommentId() : null;
    result.setParentId(parentId);
  }
}
