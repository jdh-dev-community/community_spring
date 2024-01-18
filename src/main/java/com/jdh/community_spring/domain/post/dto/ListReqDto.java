package com.jdh.community_spring.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
// TODO: hasNext, currentPage 등 Page 인스턴스의 다른 정보들을 추가
public class ListReqDto<T> {
  private long elementsCount;
  private List<T> content;

}
