package com.pavelshapel.jpa.spring.boot.starter.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@ToString
@EqualsAndHashCode
@Getter
@Setter
public abstract class AbstractEntity implements Serializable {
    public static final String MANDATORY = "mandatory";

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}