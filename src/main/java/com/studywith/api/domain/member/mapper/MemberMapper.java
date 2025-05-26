package com.studywith.api.domain.member.mapper;

import com.studywith.api.domain.member.dto.*;
import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.enums.Gender;
import com.studywith.api.domain.member.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class MemberMapper {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Member toCreateEntity(MemberCreateDTO memberCreateDTO, String loginId, String email, AccountType accountType) {
        return Member.builder()
                .loginId(loginId)
                .nickname(memberCreateDTO.getNickname())
                .email(email)
                .gender(Gender.valueOf(memberCreateDTO.getGender()))
                .birth(LocalDate.parse(memberCreateDTO.getBirth()))
                .region(memberCreateDTO.getRegion())
                .profileImage(memberCreateDTO.getProfileImage())
                .bio(memberCreateDTO.getBio())
                .accountType(accountType)
                .role(Role.MEMBER)
                .isActive(true)
                .build();
    }

    public MemberCreateDTO toCreateDTO(Member member) {
        return MemberCreateDTO.builder()
                .nickname(member.getNickname())
                .gender(member.getGender().getValue())
                .birth(member.getBirth().format(dateFormatter))
                .region(member.getRegion())
                .profileImage(member.getProfileImage())
                .bio(member.getBio())
                .build();
    }

    public MemberNicknameDTO toNicknameDTO(String nickname) {
        return MemberNicknameDTO.builder()
                .nickname(nickname)
                .build();
    }

    public MemberDetailDTO toDetailDTO(Member member) {
        return MemberDetailDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .gender(member.getGender().getValue())
                .birth(member.getBirth().format(dateFormatter))
                .region(member.getRegion())
                .profileImage(member.getProfileImage())
                .bio(member.getBio())
                .createDate(member.getCreateDate().format(dateTimeFormatter))
                .modifyDate(member.getModifyDate() != null ? member.getModifyDate().format(dateTimeFormatter) : "변경 이력이 없습니다.")
                .build();
    }

    public MemberSummaryDTO toSummaryDTO(Member member) {
        return MemberSummaryDTO.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .profileImage(member.getProfileImage())
                .role(member.getRole().getValue())
                .isActive(member.isActive())
                .build();
    }

    public MemberUpdateDTO toUpdateDTO(Member member) {
        return MemberUpdateDTO.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .region(member.getRegion())
                .profileImage(member.getProfileImage())
                .bio(member.getBio())
                .build();
    }

    public MemberHeaderDTO toHeaderDTO(Member member) {
        return MemberHeaderDTO.builder()
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .build();
    }

}
