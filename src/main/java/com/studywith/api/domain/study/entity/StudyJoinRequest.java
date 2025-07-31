package com.studywith.api.domain.study.entity;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.study.entity.id.StudyJoinRequestId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "study_join_request")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StudyJoinRequest {

    @EmbeddedId
    private StudyJoinRequestId id;

    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @MapsId("studyId")
    @JoinColumn(name = "study_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Study study;

    @CreatedDate
    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate;

}
