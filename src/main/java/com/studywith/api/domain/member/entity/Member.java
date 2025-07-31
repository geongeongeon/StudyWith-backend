package com.studywith.api.domain.member.entity;

import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.enums.Gender;
import com.studywith.api.domain.member.enums.Role;
import com.studywith.api.domain.message.entity.Message;
import com.studywith.api.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, unique = true, length = 12)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false, length = 20)
    private String region;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false, length = 100)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean isActive;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @Builder.Default
    private List<Message> receivedMessages = new ArrayList<>();

}
