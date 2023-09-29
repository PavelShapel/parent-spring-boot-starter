package com.pavelshapel.jpa.spring.boot.starter.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

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

    protected void throwUnsupportedOperationException() {
        throw new UnsupportedOperationException();
    }
}