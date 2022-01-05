package com.pavelshapel.jpa.spring.boot.starter.entity.rdb;

import com.pavelshapel.jpa.spring.boot.starter.entity.Entity;
import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class AbstractRdsEntity implements Entity<Long> {
    public static final String MANDATORY = "mandatory";

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}