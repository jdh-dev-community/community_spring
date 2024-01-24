package com.jdh.community_spring.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import javax.validation.constraints.Min;



@Data
@ToString
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class ListReqDto {

  @Schema(description = "페이지 (1부터 시작)", example = "1")
  @Min(value = 1, message = "page는 최소 1 이상이어야 합니다.")
  private int page = 1;

  @Schema(description = "페이지 사이즈", example = "10")
  @Min(value = 1, message = "size 최소 1 이상이어야 합니다.")
  private int size = 10;

  @Schema(description = "정렬 기준", example = "createdAt")
  private String sortBy = "createdAt";

  @Schema(description = "오름차순/내림차순", example = "asc/desc")
  private String orderBy = "desc";

  // NOTE: 프론트에서는 page가 1부터 시작하고 있어서 서버에서 -1을 합니다.
  public Pageable toPageable () {
    int page = this.page - 1;
    int size = this.size;
    Sort sort = this.orderBy.equals("desc")
            ? Sort.by(this.sortBy).descending()
            : Sort.by(this.sortBy).ascending();

    return PageRequest.of(page, size, sort);
  }
}
