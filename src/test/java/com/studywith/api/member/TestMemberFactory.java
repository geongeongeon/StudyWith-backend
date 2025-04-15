package com.studywith.api.member;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestMemberFactory {

    private String loginId;

    private String nickname;

    private String email;

    private String gender;

    private String birth;

    private String region;

    private String profileImage;

    private String bio;

    private String accountType;


    public static TestMemberFactory member1() {
        return new TestMemberFactory("GOOGLE_testMember1", "testMember1", "testMember1@google.com", "M", "2025-01-23",
                "서울시 강남구", null, null, "GOOGLE");
    }

    public static TestMemberFactory member2() {
        return new TestMemberFactory("NAVER_testMember2", "testMember2", "testMember2@naver.com", "F", "2025-03-12",
                "서울시 강서구", "/images/profile/2/img.png", "testMember2입니다.", "NAVER");
    }

    public static MemberCreateDTO toCreateDTO(TestMemberFactory testMember) {
        return MemberCreateDTO.builder()
                .nickname(testMember.getNickname())
                .gender(testMember.getGender())
                .birth(testMember.getBirth())
                .region(testMember.getRegion())
                .profileImage(testMember.getProfileImage())
                .bio(testMember.getBio())
                .build();
    }

}
