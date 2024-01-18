package com.jdh.community_spring.common.domain;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity {

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @CreationTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
