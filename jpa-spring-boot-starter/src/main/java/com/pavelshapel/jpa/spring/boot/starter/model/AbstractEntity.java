package com.pavelshapel.jpa.spring.boot.starter.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public abstract class AbstractEntity<ID> implements Entity<ID> {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;
}