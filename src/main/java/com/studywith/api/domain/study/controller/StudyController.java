package com.studywith.api.domain.study.controller;

import com.studywith.api.domain.study.dto.*;
import com.studywith.api.domain.study.service.StudyService;
import com.studywith.api.global.response.ApiResponse;
import com.studywith.api.global.util.SuccessResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/studies")
@RequiredArgsConstructor
public class StudyController {

    private final StudyService studyService;

    @PostMapping
    public ResponseEntity<ApiResponse<StudyCreateDTO>> createStudy(
            @RequestPart StudyCreateDTO studyCreateDTO, @RequestPart(required = false) MultipartFile thumbnailImage
    ) throws IOException {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyCreateDTO study = studyService.createStudy(studyCreateDTO, thumbnailImage, loginId);

        return SuccessResponseUtil.created("스터디가 성공적으로 생성되었습니다.", study);
    }

    @PostMapping("/{studyId}/join-requests")
    public ResponseEntity<ApiResponse<StudyJoinRequestDTO>> requestToJoinStudy(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyJoinRequestDTO joinRequest = studyService.requestToJoinStudy(studyId, loginId);

        return SuccessResponseUtil.created("스터디에 가입 신청이 되었습니다.", joinRequest);
    }

    @PostMapping("/{studyId}/join-requests/{memberId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptJoinRequest(@PathVariable("studyId") Long studyId, @PathVariable("memberId") Long memberId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.acceptJoinRequest(studyId, loginId, memberId);

        return SuccessResponseUtil.ok("스터디 가입 신청이 승인되었습니다.", null);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudySummaryDTO>>> getStudies(
            Pageable pageable, @RequestParam(defaultValue = "new") String sortBy, @RequestParam(required = false) String keyword, @RequestParam(required = false) String recruit
    ) {
        Page<StudySummaryDTO> studies = studyService.getStudies(pageable, sortBy, keyword, recruit);

        return SuccessResponseUtil.ok("스터디 목록을 성공적으로 조회했습니다.", studies);
    }

    @GetMapping("/{studyId}/join-requests")
    public ResponseEntity<ApiResponse<List<StudyJoinRequestDTO>>> getJoinRequests(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<StudyJoinRequestDTO> joinRequests = studyService.getJoinRequests(studyId, loginId);

        return SuccessResponseUtil.ok("스터디 가입 신청 목록을 성공적으로 조회했습니다.", joinRequests);
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<StudyMyDTO>> getMyStudies() {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyMyDTO studies = studyService.getMyStudies(loginId);

        return SuccessResponseUtil.ok("스터디 목록을 성공적으로 조회했습니다.", studies);
    }

    @GetMapping("/{studyId}")
    public ResponseEntity<ApiResponse<StudyDetailDTO>> getStudy(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudyDetailDTO study = studyService.getStudy(studyId, loginId);

        return SuccessResponseUtil.ok("스터디를 성공적으로 조회했습니다.", study);
    }

    @GetMapping("/{studyId}/members")
    public ResponseEntity<ApiResponse<List<StudyMemberDTO>>> getStudyMembers(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        List<StudyMemberDTO> members = studyService.getStudyMembers(studyId, loginId);

        return SuccessResponseUtil.ok("스터디 회원 목록을 성공적으로 조회했습니다.", members);
    }

    @PutMapping("/{studyId}/manager")
    public ResponseEntity<ApiResponse<Void>> delegateManager(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.delegateManager(studyId, loginId);

        return SuccessResponseUtil.ok("스터디 부관리자에게 총관리자 권한을 위임했습니다.", null);
    }

    @PutMapping("/{studyId}/recruitment")
    public ResponseEntity<ApiResponse<Void>> updateRecruitmentStatus(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.updateRecruitmentStatus(studyId, loginId);

        return SuccessResponseUtil.ok("스터디의 모집 상태가 변경되었습니다.", null);
    }

    @PutMapping("/{studyId}/sub-manager/{memberId}")
    public ResponseEntity<ApiResponse<StudySubMangerDTO>> setSubManager(@PathVariable("studyId") Long studyId, @PathVariable("memberId") Long memberId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        StudySubMangerDTO subManager = studyService.setSubManager(studyId, loginId, memberId);

        return SuccessResponseUtil.ok("스터디 부관리자가 설정되었습니다.", subManager);
    }

    @DeleteMapping("/{studyId}")
    public ResponseEntity<ApiResponse<Void>> deleteStudy(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.deleteStudy(studyId, loginId);

        return SuccessResponseUtil.ok("스터디가 성공적으로 삭제되었습니다.", null);
    }

    @DeleteMapping("/{studyId}/join-requests/{memberId}")
    public ResponseEntity<ApiResponse<Void>> declineJoinRequest(@PathVariable("studyId") Long studyId, @PathVariable("memberId") Long memberId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.declineJoinRequest(studyId, loginId, memberId);

        return SuccessResponseUtil.ok("스터디 가입 신청이 거절되었습니다.", null);
    }

    @DeleteMapping("/{studyId}/members")
    public ResponseEntity<ApiResponse<Void>> kickAllMembers(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.kickAll(studyId, loginId);

        return SuccessResponseUtil.ok("스터디의 모든 회원이 탈퇴되었습니다.", null);
    }

    @DeleteMapping("/{studyId}/members/{memberId}")
    public ResponseEntity<ApiResponse<Void>> kickMember(@PathVariable("studyId") Long studyId, @PathVariable("memberId") Long memberId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.kickMember(studyId, loginId, memberId);

        return SuccessResponseUtil.ok("스터디의 회원이 탈퇴되었습니다.", null);
    }

    @DeleteMapping("/{studyId}/members/me")
    public ResponseEntity<ApiResponse<Void>> leaveStudy(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.leaveStudy(studyId, loginId);

        return SuccessResponseUtil.ok("스터디에서 성공적으로 탈퇴되었습니다.", null);
    }

    @DeleteMapping("/{studyId}/sub-manager")
    public ResponseEntity<ApiResponse<Void>> unsetSubManager(@PathVariable("studyId") Long studyId) {
        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();
        studyService.unsetSubManager(studyId, loginId);

        return SuccessResponseUtil.ok("스터디 부관리자가 해제되었습니다.", null);
    }

}
