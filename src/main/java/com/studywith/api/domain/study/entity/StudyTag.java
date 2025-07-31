package com.studywith.api.domain.study.entity;

import com.studywith.api.domain.study.entity.id.StudyTagId;
import com.studywith.api.domain.tag.entity.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "study_tag")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class StudyTag {

    @EmbeddedId
    private StudyTagId id;

    @MapsId("studyId")
    @JoinColumn(name = "study_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

}
