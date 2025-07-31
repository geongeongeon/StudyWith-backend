package com.studywith.api.domain.study.entity;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.study.entity.id.StudyMemberId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_member")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class StudyMember {

    @EmbeddedId
    private StudyMemberId id;

    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @MapsId("studyId")
    @JoinColumn(name = "study_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @CreatedDate
    @Column(name = "join_date", nullable = false)
    private LocalDateTime joinDate;

}
