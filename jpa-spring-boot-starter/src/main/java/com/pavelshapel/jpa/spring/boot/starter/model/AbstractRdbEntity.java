package com.pavelshapel.jpa.spring.boot.starter.model;

import com.pavelshapel.core.spring.boot.starter.model.Entity;
import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class AbstractRdbEntity implements Entity<Long> {
    public static final String MANDATORY = "mandatory";

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}