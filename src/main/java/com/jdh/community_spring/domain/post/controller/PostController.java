package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.common.dto.HttpErrorInfo;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.common.dto.ListReqDto;
import com.jdh.community_spring.domain.post.dto.CreateReqDto;
import com.jdh.community_spring.domain.post.service.interfaces.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {

  private final PostService postService;


  @Operation(summary = "게시글 생성", description = "제목, 내용, 작성자, 카테고리를 포함하는 게시글을 작성합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "201", description = "게시물 생성 성공하였습니다."),
          @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력 데이터를 확인하세요."),
          @ApiResponse(responseCode = "500", description = "서버 내부 오류가 발생했습니다.")
  })
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post")
  public void createPost(@Valid @RequestBody CreateReqDto dto) {
    postService.createPost(dto);
  }



  @GetMapping("/post")
  public ListReqDto<Post> getPostList(
          @RequestParam(defaultValue = "1") int page,
          @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page - 1, size);
    ListReqDto<Post> dto = postService.getPostList(pageable);

    return dto;
  }
}
