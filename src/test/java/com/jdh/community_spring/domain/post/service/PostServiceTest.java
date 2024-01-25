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
import com.jdh.community_spring.domain.post.service.mapper.PostMapper;
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
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
  @Mock
  private SimpleEncrypt simpleEncrypt;
  @Mock
  private InMemoryDBProvider inMemoryDBProvider;
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
      when(postMapper.toEntity(dto, simpleEncrypt)).thenReturn(entity);

      postService.createPost(dto);
    }

    @Test
    public void 인풋이_null인경우_InvalidDataAccessApiUsageException이발생() {
      CreateReqDto dto = null;
      when(postMapper.toEntity(dto, simpleEncrypt)).thenThrow(InvalidDataAccessApiUsageException.class);
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
    public void 인풋이_유효한ListReqDto인경우_ListResDto를반환() {
      ListReqDto dto = new ListReqDto(1, 10, "createdAt", "desc");
      Pageable pageable = dto.toPageable();


      when(postRepository.findAll(pageable)).thenReturn(createDummy(pageable, TOTAL_COUNT));
      when(postMapper.toPostResDto(any(Post.class)))
              .thenAnswer(invocation -> {
                Post post = invocation.getArgument(0);
                return new PostResDto(post.getPostId(), post.getTitle(), post.getTextContent(), post.getCategory(), post.getCreator(), post.getViewCount(), post.getCreatedAt());
              });

      ListResDto<PostResDto> result = postService.getPostList(dto);
      assertThat(result.getElementsCount()).isEqualTo(TOTAL_COUNT);
      assertThat(result.getContent().size()).isLessThanOrEqualTo(PAGE_SIZE);
    }

    @Test
    public void ListReqDto의_orderBy가desc인경우_내림차순정렬() {
      ListReqDto input = new ListReqDto(1, 10, "createdAt", "desc");
      Pageable pageable = input.toPageable();

      when(postRepository.findAll(pageable)).thenReturn(createDummy(pageable, TOTAL_COUNT));
      when(postMapper.toPostResDto(any(Post.class)))
              .thenAnswer(invocation -> {
                Post post = invocation.getArgument(0);
                return new PostResDto(post.getPostId(), post.getTitle(), post.getTextContent(), post.getCategory(), post.getCreator(), post.getViewCount(), post.getCreatedAt());
              });


      ListResDto<PostResDto> result = postService.getPostList(input);

      assertThat(result.getContent().size()).isEqualTo(10);
      assertThat(result.getContent().get(1).getCreatedAt()).isAfterOrEqualTo(result.getContent().get(0).getCreatedAt());
    }

    private Page<Post> createDummy(Pageable pageable, int totalElements) {
      String HashedPassword = "Fake Hashed";
      List<Post> list = IntStream.rangeClosed(1, pageable.getPageSize())
              .mapToObj((i) -> new Post(i, "title" + i, "text", "creator", "category", 0, HashedPassword))
              .collect(Collectors.toList());

      return new PageImpl<>(list, pageable, totalElements);
    }
  }


  @DisplayName("게시글상세서비스")
  @Nested
  class GetPost {

    @Test
    public void 인풋이_long타입으로변환되고_매치되는게시글이있다면_PostResDto반환() {
      String validId = "353";
      long id = Long.parseLong(validId);

      Post dummyPost = new Post(id,"제목", "컨텐츠", "작성자", "카테고리", 10, simpleEncrypt.encrypt("1234"));
      PostResDto dummyResult = new PostResDto(id, "제목", "컨텐츠", "작성자", "카테고리", 10, dummyPost.getCreatedAt());

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


  @DisplayName("게시글수정삭제인증")
  @Nested
  class GenerateToken {
    @Test
    public void 매칭되는_게시물이없는경우_NotFoundException발생() {
      PostAuthReqDto dummyDto = new PostAuthReqDto(1000L, "1234");

      when(postRepository.findById(dummyDto.getPostId())).thenThrow(NotFoundException.class);
      assertThrows(NotFoundException.class, () -> postService.generateToken(dummyDto));
    }

    @Test
    public void 비밀번호가_잘못된경우_IllegalArgumentException발생() {
      PostAuthReqDto dummyDto = new PostAuthReqDto(1000L, "1234");
      Post dummyPost = new Post("제목", "내용", "작성자", "카테고리", "비밀번호");

      when(postRepository.findById(dummyDto.getPostId())).thenReturn(Optional.ofNullable(dummyPost));
      when(simpleEncrypt.match(dummyDto.getPassword(), dummyPost.getPassword())).thenReturn(false);
      assertThrows(IllegalArgumentException.class, () -> postService.generateToken(dummyDto));
    }

    @Test
    public void 비밀번호가_일치하는경우_token반환() {
      PostAuthReqDto dummyDto = new PostAuthReqDto(1000, "1234");
      Post dummyPost = new Post("제목", "내용", "작성자", "카테고리", "1234");
      String dummyToken = "Dummy Token";

      when(postRepository.findById(dummyDto.getPostId())).thenReturn(Optional.ofNullable(dummyPost));
      when(simpleEncrypt.match(dummyDto.getPassword(), dummyPost.getPassword())).thenReturn(true);
      when(simpleEncrypt.encrypt(dummyDto.getPostId() + dummyDto.getPassword())).thenReturn(dummyToken);

      String token = postService.generateToken(dummyDto);

      assertEquals(token, dummyToken);
      verify(inMemoryDBProvider, times(1)).setTemperarily(eq(String.valueOf(dummyDto.getPostId())), eq(token), eq((long) 3 * 60));
    }


  }

}
