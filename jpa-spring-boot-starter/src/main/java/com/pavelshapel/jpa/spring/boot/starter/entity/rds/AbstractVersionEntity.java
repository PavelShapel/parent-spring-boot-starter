package com.pavelshapel.jpa.spring.boot.starter.entity.rds;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@MappedSuperclass
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractVersionEntity extends AbstractEntity {
    @Version
    @Column
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Long version;
}