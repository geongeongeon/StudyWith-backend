package com.studywith.api.member;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import com.studywith.api.domain.member.dto.MemberUpdateDTO;

public class TestMemberFactory {

    public static MemberCreateDTO getFirstMemberCreateDTO() {
        return new MemberCreateDTO("testMember1", "M", "2025-01-23", "서울시 강남구", null, null);
    }

    public static MemberCreateDTO getSecondMemberCreateDTO() {
        return new MemberCreateDTO("testMember2", "F", "2025-03-12", "서울시 강서구", "/images/profile/2/img.png", "testMember2입니다."
        );
    }

    public static  MemberUpdateDTO getFirstMemberUpdateDTO() {
        return new MemberUpdateDTO("testMember1", "서울시 강남구", "/images/profile/1/img.png", "안녕하세요. 만나서 반가워요!");
    }

    public static MemberUpdateDTO getSecondMemberUpdateDTO() {
        return new MemberUpdateDTO("testMember2", "서울시 강서구", "/images/profile/2/img.png", "testMember2입니다.");
    }

}
