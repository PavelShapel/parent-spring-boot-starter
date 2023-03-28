package com.pavelshapel.jpa.spring.boot.starter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pavelshapel.core.spring.boot.starter.api.model.Versioned;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractVersionedEntity<ID> extends AbstractEntity<ID> implements Versioned {
    @Version
    @Column
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    Long version;

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public void setVersion(Long version) {
        throwUnsupportedOperationException();
    }
}