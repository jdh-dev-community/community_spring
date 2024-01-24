package com.jdh.community_spring.domain.post.controller;

import com.jdh.community_spring.common.util.SimplePasswordEncoder;
import com.jdh.community_spring.domain.post.domain.Post;
import com.jdh.community_spring.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/dummy")
public class PostDummyController {

  @Autowired
  private final PostRepository postRepository;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/post")
  public void createPost(@RequestBody Map<String, Integer> body) {

    Integer size = body.get("size");
    List<Post> list = IntStream.rangeClosed(1, size)
            .mapToObj((i) -> new Post("제목" + i, "이건 더미 데이터", "테스트", "테스트", SimplePasswordEncoder.encode("1234")))
            .collect(Collectors.toList());

    postRepository.saveAll(list);
  }
}
