package com.pavelshapel.jpa.spring.boot.starter.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@MappedSuperclass
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractEntity<ID> implements Entity<ID> {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ID id;

    @Override
    public int compareTo(Entity<ID> entity) {
        return ((Comparable<ID>) getId()).compareTo(entity.getId());
    }
}