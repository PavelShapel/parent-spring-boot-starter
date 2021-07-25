package com.pavelshapel.jpa.spring.boot.starter.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Data
public abstract class AbstractEntity implements Serializable {
    public static final String MANDATORY = "mandatory";

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}