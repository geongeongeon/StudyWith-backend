package com.studywith.api.domain.member.controller;

import com.studywith.api.domain.member.dto.*;
import com.studywith.api.domain.member.service.MemberService;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.ResponseHeaderUtil;
import com.studywith.api.global.util.SuccessResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberCreateDTO>> createMember(
            @RequestPart MemberCreateDTO memberCreateDTO, @RequestPart(required = false) MultipartFile profileImage, HttpServletRequest request, HttpServletResponse response
    ) throws IOException {
        String uuid = ResponseHeaderUtil.getCookie(request, "UUID");
        MemberCreateDTO createdMember = memberService.createMember(memberCreateDTO, profileImage, uuid);
        ResponseHeaderUtil.setCookie(response, "UUID", "", 0L);

        return SuccessResponseUtil.created("회원이 성공적으로 가입되었습니다.", createdMember);
    }

    @GetMapping("/exists")
    public ResponseEntity<ApiResponse<MemberNicknameDTO>> isNicknameExists(@RequestParam("nickname") String nicknameParam) {
        MemberNicknameDTO nickname = memberService.isNicknameExists(nicknameParam);

        return SuccessResponseUtil.ok("사용 가능한 별명입니다.", nickname);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberDetailDTO>> getMemberById(@PathVariable("id") Long id) {
        MemberDetailDTO member = memberService.getMemberDetailById(id);

        return SuccessResponseUtil.ok("회원을 성공적으로 조회했습니다.", member);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberDetailDTO>> getMe() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberDetailDTO member = memberService.getMemberDetailByLoginId(loginId);

        return SuccessResponseUtil.ok("회원을 성공적으로 조회했습니다.", member);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MemberSummaryDTO>>> getMembers() {
        List<MemberSummaryDTO> members = memberService.getMembers();

        return SuccessResponseUtil.ok("회원 목록을 성공적으로 조회했습니다.", members);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberUpdateDTO>> updateMember(
            @PathVariable("id") Long id, @RequestPart MemberUpdateDTO memberUpdateDTO, @RequestPart(required = false) MultipartFile profileImage
    ) throws IOException {
        MemberUpdateDTO updatedMember = memberService.updateMember(id, memberUpdateDTO, profileImage);

        return SuccessResponseUtil.ok("회원 정보가 성공적으로 조회했습니다.", updatedMember);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<MemberDetailDTO>> updateMe(
            @RequestPart MemberUpdateDTO memberUpdateDTO, @RequestPart(required = false) MultipartFile profileImage
    ) throws IOException {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        MemberDetailDTO updatedMember = memberService.updateMe(loginId, memberUpdateDTO, profileImage);

        return SuccessResponseUtil.ok("회원 정보가 성공적으로 변경되었습니다.", updatedMember);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable("id") Long id, @CookieValue(name = "REFRESH_TOKEN", required = false) String refreshToken)  {
        memberService.deleteMember(refreshToken, id);

        return SuccessResponseUtil.ok("회원이 성공적으로 탈퇴되었습니다.", null);
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> deleteMe(@CookieValue(name = "REFRESH_TOKEN", required = false) String refreshToken) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        memberService.deleteMe(refreshToken, loginId);

        return SuccessResponseUtil.ok("회원이 성공적으로 탈퇴되었습니다.", null);
    }

}
