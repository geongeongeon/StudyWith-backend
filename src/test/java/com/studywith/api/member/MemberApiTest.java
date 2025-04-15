package com.studywith.api.member;

import com.studywith.api.domain.member.dto.MemberDetailDTO;
import com.studywith.api.domain.member.dto.MemberSummaryDTO;
import com.studywith.api.domain.member.dto.MemberUpdateDTO;
import com.studywith.api.domain.member.exception.MemberNicknameAlreadyInUseException;
import com.studywith.api.domain.member.exception.MemberNoChangesException;
import com.studywith.api.domain.member.exception.MemberNotFoundException;
import com.studywith.api.domain.member.repository.MemberRepository;
import com.studywith.api.domain.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
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

    long initialMemberCount;
    long firstMemberId;
    long secondMemberId;

    @BeforeEach
    void setUp() {
        initialMemberCount = memberRepository.count();
        memberService.createMember(TestMemberFactory.getFirstMemberCreateDTO(), "GOOGLE_testMember1", "testMember1@google.com", "GOOGLE");
        memberService.createMember(TestMemberFactory.getSecondMemberCreateDTO(), "NAVER_testMember2", "testMember2@naver.com", "NAVER");
        secondMemberId = memberRepository.getLastId();
        firstMemberId = secondMemberId - 1;
    }

    @Test
    @DisplayName("회원 생성하기")
    void t001() {
        MemberDetailDTO testMember1 = memberService.getMemberById(firstMemberId);

        assertThat(memberRepository.count()).isEqualTo(initialMemberCount + 2);
        assertThat(testMember1.getNickname()).isEqualTo("testMember1");
        assertThat(testMember1.getProfileImage()).isEqualTo("/images/profile/default/male.png");
        assertThat(testMember1.getBio()).isEqualTo("안녕하세요. 만나서 반가워요!");
    }

    @Test
    @DisplayName("별명 중복 검사하기")
    void t002() {
        String nickname = memberService.isNicknameExists("testMember3").getNickname();

        assertThat(nickname).isEqualTo("testMember3");

        assertThatThrownBy(() -> memberService.isNicknameExists("testMember1")).isInstanceOf(MemberNicknameAlreadyInUseException.class);
        assertThatThrownBy(() -> memberService.isNicknameExists("testMember2")).isInstanceOf(MemberNicknameAlreadyInUseException.class);
    }

    @Test
    @DisplayName("회원 조회하기")
    void t003() {
        MemberDetailDTO testMember1 = memberService.getMemberById(firstMemberId);

        assertThatThrownBy(() -> memberService.getMemberById(0L)).isInstanceOf(MemberNotFoundException.class);
        assertThat(testMember1.getNickname()).isEqualTo("testMember1");
    }

    @Test
    @DisplayName("회원 목록 조회하기")
    void t004() {
        List<MemberSummaryDTO> members = memberService.getMembers();
        MemberSummaryDTO testMember1 = members.get(members.size() - 2);
        MemberSummaryDTO testMember2 = members.get(members.size() - 1);

        assertThat(testMember1.getNickname()).isEqualTo("testMember1");
        assertThat(testMember2.getNickname()).isEqualTo("testMember2");
    }

    @Test
    @DisplayName("회원 수정하기")
    void t005() {
        MemberUpdateDTO testMember1 = memberService.updateMember(firstMemberId, TestMemberFactory.getFirstMemberUpdateDTO());

        assertThat(testMember1.getNickname()).isEqualTo("testMember1");
        assertThat(testMember1.getProfileImage()).isEqualTo("/images/profile/1/img.png");

        assertThatThrownBy(() -> memberService.updateMember(secondMemberId, TestMemberFactory.getSecondMemberUpdateDTO())).isInstanceOf(MemberNoChangesException.class);
    }

    @Test
    @DisplayName("회원 삭제하기")
    void t006() {
        memberService.deleteMember(firstMemberId);
        memberService.deleteMember(secondMemberId);

        assertThat(memberRepository.count()).isEqualTo(initialMemberCount);

        assertThatThrownBy(() -> memberService.deleteMember(firstMemberId)).isInstanceOf(MemberNotFoundException.class);
    }

}
