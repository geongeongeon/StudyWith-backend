package com.studywith.api;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.repository.MemberRepository;
import com.studywith.api.domain.member.service.MemberService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ApiApplicationTests {

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private MemberService memberService;

    @Test
	@DisplayName("회원 생성하기")
	void t001() {
		long countMembers = memberRepository.count();

		MemberCreateDTO createDTO = new MemberCreateDTO("testUser01", "M", "2025-04-13", "서울시 강남구", null, null);
		MemberCreateDTO member = memberService.createMember(createDTO, "testUserLoginId01", "testUser01@google.com", AccountType.GOOGLE);

		assertThat(memberRepository.count()).isEqualTo(countMembers + 1L);
		assertThat(member.getNickname()).isEqualTo("testUser01");
		assertThat(member.getProfileImage()).isEqualTo("/images/male.png");
		assertThat(member.getBio()).isEqualTo("안녕하세요. 만나서 반가워요!");
	}

}
