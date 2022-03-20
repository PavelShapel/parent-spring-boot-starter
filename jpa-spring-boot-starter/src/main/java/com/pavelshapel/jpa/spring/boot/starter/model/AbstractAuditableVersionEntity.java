package com.pavelshapel.jpa.spring.boot.starter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractAuditableVersionEntity extends AbstractVersionEntity {
    @CreatedBy
    @Column
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private String createdBy;

    @CreatedDate
    @Column
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Date createdDate;

    @LastModifiedBy
    @Column
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private String lastModifiedBy;

    @LastModifiedDate
    @Column
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Date lastModifiedDate;
}