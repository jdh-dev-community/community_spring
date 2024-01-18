package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.exception.InvalidInputException;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.dto.ListReqDto;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

  @Mock
  private PostRepository postRepository;
  @InjectMocks
  private PostServiceImpl postService;


  @Nested
  class 게시글목록서비스 {

    private final int PAGE = 0;
    private final int PAGE_SIZE = 10;

    private final int TOTAL_COUNT = 50;

    @Test
    public void Pageable이인풋으로들어오면ListReqDto를반환한다() {
      Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);
      when(postRepository.findAll(pageable)).thenReturn(createDummy(pageable, TOTAL_COUNT));

      ListReqDto<Post> result = postService.getPostList(pageable);
      assertThat(result.getElementsCount()).isEqualTo(TOTAL_COUNT);
      assertThat(result.getContent().size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @Test
    public void Pageable이Null인경우는InvalidInputException발생한다() {
      Pageable pageable = null;
      assertThrows(InvalidInputException.class, () -> postService.getPostList(pageable));
    }

    private Page<Post> createDummy(Pageable pageable, int totalElements) {
      List<Post> list = IntStream.rangeClosed(1, pageable.getPageSize())
              .mapToObj((i) -> new Post("제목" + i, "이건 더미 데이터", "테스트", "테스트"))
              .collect(Collectors.toList());


      return new PageImpl<>(list, pageable, totalElements);
    }
  }

}
