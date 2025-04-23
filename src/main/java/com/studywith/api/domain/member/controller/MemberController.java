package com.studywith.api.domain.member.controller;

import com.studywith.api.domain.member.dto.*;
import com.studywith.api.domain.member.service.MemberService;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.ResponseHeaderUtil;
import com.studywith.api.global.util.SuccessResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public ResponseEntity<ApiResponse<MemberCreateDTO>> createMember(@Valid @RequestBody MemberCreateDTO memberCreateDTO, HttpServletRequest request, HttpServletResponse response) {
        String uuid = ResponseHeaderUtil.getCookie(request, "UUID");
        MemberCreateDTO createdMember = memberService.createMember(memberCreateDTO, uuid);
        ResponseHeaderUtil.setCookie(response, "UUID", "", 0L);

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
    public ResponseEntity<ApiResponse<MemberUpdateDTO>> updateMember(@PathVariable("id") Long id, @Valid @RequestBody MemberUpdateDTO memberUpdateDTO) {
        MemberUpdateDTO updatedMember = memberService.updateMember(id, memberUpdateDTO);

        return SuccessResponseUtil.ok("회원이 성공적으로 수정되었습니다.", updatedMember);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);

        return SuccessResponseUtil.ok("회원이 성공적으로 삭제되었습니다.", null);
    }

}
