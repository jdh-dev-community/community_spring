package com.jdh.community_spring.domain.post.service;

import com.jdh.community_spring.common.exception.InvalidInputException;
import com.jdh.community_spring.common.exception.NotFoundException;
import com.jdh.community_spring.common.util.SimplePasswordEncoder;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.dto.PostResDto;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

  @Mock
  private PostMapper postMapper;
  @Mock
  private PostRepository postRepository;
  @InjectMocks
  private PostServiceImpl postService;


  @DisplayName("게시글생성하기서비스")
  @Nested
  class CreatePost {
    @Test
    public void 인풋이_유효한경우_db에저장() {
      CreateReqDto dto = new CreateReqDto("제목1", "내용", "카테고리", "작성자", "1234");
      Post entity = new Post();
      when(postMapper.toEntity(dto)).thenReturn(entity);

      postService.createPost(dto);
    }

    @Test
    public void 인풋이_null인경우_InvalidDataAccessApiUsageException이발생() {
      CreateReqDto dto = null;
      when(postMapper.toEntity(dto)).thenThrow(InvalidDataAccessApiUsageException.class);
      assertThrows(InvalidDataAccessApiUsageException.class,() -> postService.createPost(dto));
    }
  }

  @DisplayName("게시글목록서비스")
  @Nested
  class GetPostList {

    private final int PAGE = 0;
    private final int PAGE_SIZE = 10;
    private final int TOTAL_COUNT = 50;

    @Test
    public void 인풋에_유효한Pageable인경우_ListReqDto를반환() {
      Pageable pageable = PageRequest.of(PAGE, PAGE_SIZE);
      when(postRepository.findAll(pageable)).thenReturn(createDummy(pageable, TOTAL_COUNT));

      ListReqDto<Post> result = postService.getPostList(pageable);
      assertThat(result.getElementsCount()).isEqualTo(TOTAL_COUNT);
      assertThat(result.getContent().size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @Test
    public void 인풋이_Null인경우_InvalidInputException발생() {
      Pageable pageable = null;
      assertThrows(IllegalArgumentException.class, () -> postService.getPostList(pageable));
    }

    private Page<Post> createDummy(Pageable pageable, int totalElements) {
      List<Post> list = IntStream.rangeClosed(1, pageable.getPageSize())
              .mapToObj((i) -> new Post("제목" + i, "이건 더미 데이터", "테스트", "테스트", SimplePasswordEncoder.encode("1234")))
              .collect(Collectors.toList());


      return new PageImpl<>(list, pageable, totalElements);
    }
  }


  @DisplayName("게시글목록서비스")
  @Nested
  class GetPost {

    @Test
    public void 인풋이_long타입으로변환되고_매치되는게시글이있다면_PostResDto반환() {
      String validId = "353";
      long id = Long.parseLong(validId);

      Post dummyPost = new Post(id,"제목", "컨텐츠", "작성자", "카테고리", 10, SimplePasswordEncoder.encode("1234"));
      PostResDto dummyResult = new PostResDto(id, "제목", "컨텐츠", "작성자", "카테고리", 10, LocalDateTime.now());

      when(postRepository.findById(id)).thenReturn(Optional.of(dummyPost));
      when(postMapper.toPostResDto(dummyPost)).thenReturn(dummyResult);

      PostResDto result = postService.getPost(validId);

      assertThat(result.getPostId()).isEqualTo(id);
    }

    @Test
    public void 인풋이_long타입으로변환되지만_매치되는게시글이없다면_NotFound예외발생() {
      String notMatchedId = "10000";
      long id = Long.parseLong(notMatchedId);

      when(postRepository.findById(id)).thenReturn(Optional.empty());
      assertThrows(NotFoundException.class, () -> postService.getPost(notMatchedId));
    }

    @Test
    public void 인풋이_long타입으로변환되지않는경우_NumberForMatException발생() {
      String invalidId = "a";
      assertThrows(NumberFormatException.class, () -> postService.getPost(invalidId));
    }


  }

}
