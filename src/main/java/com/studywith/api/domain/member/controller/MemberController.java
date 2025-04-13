package com.studywith.api.domain.member.controller;

import com.studywith.api.domain.member.dto.MemberCreateDTO;
import com.studywith.api.domain.member.enums.AccountType;
import com.studywith.api.domain.member.service.MemberService;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.SuccessResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberCreateDTO>> createMember(@RequestBody MemberCreateDTO memberCreateDTO) {
        MemberCreateDTO createdMember = memberService.createMember(memberCreateDTO, "testUserLoginId01", "testUser01@google.com", AccountType.GOOGLE);

        return SuccessResponseUtil.created("회원가입이 성공적으로 완료되었습니다.", createdMember);
    }

}
