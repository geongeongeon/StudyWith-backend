package com.studywith.api.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TestMemberData {

    private String loginId;

    private String nickname;

    private String email;

    private String gender;

    private String birth;

    private String region;

    private String profileImage;

    private String bio;

    private String accountType;


    public static TestMemberData member1() {
        return new TestMemberData("GOOGLE_testMember1", "testMember1", "testMember1@google.com", "M", "2025-01-23",
                "서울시 강남구", null, null, "GOOGLE");
    }

    public static TestMemberData member2() {
        return new TestMemberData("NAVER_testMember2", "testMember2", "testMember2@naver.com", "F", "2025-03-12",
                "서울시 강서구", "/images/profile/2/img.png", "testMember2입니다.", "NAVER");
    }

}
