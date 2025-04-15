package com.studywith.api.domain.member.controller;

import com.studywith.api.domain.member.dto.*;
import com.studywith.api.domain.member.service.MemberService;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.SuccessResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberCreateDTO>> createMember(@Valid @RequestBody MemberCreateDTO memberCreateDTO) {
        MemberCreateDTO createdMember = memberService.createMember(memberCreateDTO, "testMemberLoginId1", "testMember1@google.com","GOOGLE");

        return SuccessResponseUtil.created("회원 가입이 성공적으로 완료되었습니다.", createdMember);
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<MemberNicknameDTO>> isNicknameExists(@RequestParam("nickname") String nicknameParam) {
        MemberNicknameDTO nickname = memberService.isNicknameExists(nicknameParam);

        return SuccessResponseUtil.ok("사용 가능한 별명입니다.", nickname);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberDetailDTO>> getMemberById(@PathVariable("id") Long id) {
        MemberDetailDTO member = memberService.getMemberById(id);

        return SuccessResponseUtil.ok("회원을 성공적으로 조회했습니다.", member);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberSummaryDTO>>> getMembers() {
        List<MemberSummaryDTO> members = memberService.getMembers();

        return SuccessResponseUtil.ok("회원 목록을 성공적으로 불러왔습니다.", members);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberUpdateDTO>> partialUpdateMember(@PathVariable("id") Long id, @Valid @RequestBody MemberUpdateDTO memberUpdateDTO) {
        MemberUpdateDTO updatedMember = memberService.updateMember(id, memberUpdateDTO);

        return SuccessResponseUtil.ok("회원을 성공적으로 수정했습니다.", updatedMember);
    }

}
