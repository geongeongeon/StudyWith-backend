package com.studywith.api.domain.study.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@EqualsAndHashCode
public class StudyTagId implements Serializable {

    @Column(name = "study_id")
    private Long studyId;

    @Column(name = "tag_id")
    private Long tagId;

}
