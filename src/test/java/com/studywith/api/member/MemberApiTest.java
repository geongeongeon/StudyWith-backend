package com.studywith.api.member;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import com.studywith.api.domain.member.dto.MemberDetailDTO;
import com.studywith.api.domain.member.dto.MemberSummaryDTO;
import com.studywith.api.domain.member.repository.MemberRepository;
import com.studywith.api.domain.member.service.MemberService;
import com.studywith.api.global.exception.NicknameAlreadyInUseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class MemberApiTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    private MemberCreateDTO createTestMember(TestMemberData testMember) {
        MemberCreateDTO member = MemberCreateDTO.builder()
                .nickname(testMember.getNickname())
                .gender(testMember.getGender())
                .birth(testMember.getBirth())
                .region(testMember.getRegion())
                .profileImage(testMember.getProfileImage())
                .bio(testMember.getBio())
                .build();

        return memberService.createMember(member, testMember.getLoginId(), testMember.getEmail(), testMember.getAccountType());
    }

    @Test
    @DisplayName("회원 생성하기")
    void t001() {
        long countMembers = memberRepository.count();
        MemberCreateDTO testMember1 = createTestMember(TestMemberData.member1());

        assertThat(memberRepository.count()).isEqualTo(countMembers + 1);
        assertThat(testMember1.getProfileImage()).isEqualTo("/images/profile/default/male.png");
        assertThat(testMember1.getBio()).isEqualTo("안녕하세요. 만나서 반가워요!");
    }

    @Test
    @DisplayName("별명 중복 검사하기")
    void t002() {
        createTestMember(TestMemberData.member1());
        String nickname = memberService.isNicknameExists("testMember3").getNickname();

        assertThatThrownBy(() -> memberService.isNicknameExists("testMember1")).isInstanceOf(NicknameAlreadyInUseException.class);
        assertThat(nickname).isEqualTo("testMember3");
    }

    @Test
    @DisplayName("회원 조회하기")
    void t003() {
        createTestMember(TestMemberData.member1());
        MemberDetailDTO testMember1 = memberService.getMemberById(memberRepository.getLastId());

        assertThat(testMember1.getNickname()).isEqualTo("testMember1");
    }

    @Test
    @DisplayName("회원 목록 조회하기")
    void t004() {
        createTestMember(TestMemberData.member1());
        createTestMember(TestMemberData.member2());
        List<MemberSummaryDTO> members = memberService.getMembers();
        MemberSummaryDTO testMember1 = members.get(members.size() - 2);
        MemberSummaryDTO testMember2 = members.get(members.size() - 1);

        assertThat(testMember1.getNickname()).isEqualTo("testMember1");
        assertThat(testMember2.getNickname()).isEqualTo("testMember2");
    }

}
