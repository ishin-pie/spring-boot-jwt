package com.ishinvin.backend.springboot.jwt.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseDomain {

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_datetime")
    private Date createdDateTime;

    @Column(name = "modified_by")
    private Long modifiedBy;

    @Column(name = "modified_datetime")
    private Date modifiedDateTime;

    @Version
    @Column(name = "version")
    private Integer version;

}
