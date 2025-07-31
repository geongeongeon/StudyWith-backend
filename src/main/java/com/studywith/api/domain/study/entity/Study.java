package com.studywith.api.domain.study.entity;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Study extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Member manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_manager_id")
    private Member subManager;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, length = 20)
    private String region;

    @Column(nullable = false)
    private String thumbnailImage;

    @Column(nullable = false)
    private boolean isRecruit;

    @Column(nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<StudyTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "study", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<StudyMember> members = new ArrayList<>();

}
