package com.studywith.api.domain.study.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudyMemberId implements Serializable {

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "study_id")
    private Long studyId;

}
