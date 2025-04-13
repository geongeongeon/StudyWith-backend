package com.studywith.api.domain.member.mapper;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.enums.Gender;
import com.studywith.api.domain.member.enums.Role;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MemberMapper {

    public Member toEntity(MemberCreateDTO memberCreateDTO, String loginId, String email, AccountType accountType) {
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
        return new MemberCreateDTO(
                member.getNickname(),
                member.getGender().toString(),
                member.getBirth().toString(),
                member.getRegion(),
                member.getProfileImage(),
                member.getBio()
        );
    }

}
