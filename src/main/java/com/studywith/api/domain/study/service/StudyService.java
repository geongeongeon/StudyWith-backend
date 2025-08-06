package com.studywith.api.domain.study.service;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.exception.MemberNotFoundException;
import com.studywith.api.domain.member.repository.MemberRepository;
import com.studywith.api.domain.study.dto.*;
import com.studywith.api.domain.study.entity.Study;
import com.studywith.api.domain.study.entity.StudyJoinRequest;
import com.studywith.api.domain.study.entity.StudyMember;
import com.studywith.api.domain.study.entity.StudyTag;
import com.studywith.api.domain.study.entity.id.StudyJoinRequestId;
import com.studywith.api.domain.study.entity.id.StudyMemberId;
import com.studywith.api.domain.study.entity.id.StudyTagId;
import com.studywith.api.domain.study.exception.*;
import com.studywith.api.domain.study.mapper.StudyMapper;
import com.studywith.api.domain.study.repository.StudyJoinRequestRepository;
import com.studywith.api.domain.study.repository.StudyMemberRepository;
import com.studywith.api.domain.study.repository.StudyRepository;
import com.studywith.api.domain.study.repository.StudyTagRepository;
import com.studywith.api.domain.tag.entity.Tag;
import com.studywith.api.domain.tag.repository.TagRepository;
import com.studywith.api.global.common.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyJoinRequestRepository studyJoinRequestRepository;
    private final StudyMapper studyMapper;
    private final ImageService imageService;
    private final TagRepository tagRepository;
    private final StudyTagRepository studyTagRepository;

    private final String STUDY_THUMBNAIL_IMAGE_PREFIX = "uploads/studies/";

    @Transactional
    public StudyCreateDTO createStudy(StudyCreateDTO studyCreateDTO, MultipartFile thumbnailImage, String loginId) throws IOException {
        if (thumbnailImage != null && !thumbnailImage.isEmpty()) {
            String thumbnailImagePath = imageService.upload(thumbnailImage, STUDY_THUMBNAIL_IMAGE_PREFIX);
            studyCreateDTO.setThumbnailImage(thumbnailImagePath);
        } else studyCreateDTO.setThumbnailImage(imageService.getImagePrefix() + STUDY_THUMBNAIL_IMAGE_PREFIX + "default/study-with.jpg");

        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        Study study = studyRepository.save(studyMapper.toCreateEntity(studyCreateDTO, member));
        createStudyTags(study, studyCreateDTO.getTags());
        StudyMember studyMember = new StudyMember();
        studyMember.setId(new StudyMemberId()); // @MapsId에 의해 member, study 정보로 내부의 id 값들이 자동으로 설정됨
        studyMember.setMember(member);
        studyMember.setStudy(study);
        studyMemberRepository.save(studyMember);

        return studyMapper.toCreateDTO(study);
    }

    @Transactional
    private void createStudyTags(Study study, List<String> tagNames) {
        if (tagNames == null) return;

        for (String tagName : tagNames) {
            if (tagName == null || tagName.trim().isEmpty()) continue;

            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));

            StudyTag studyTag = new StudyTag();
            studyTag.setId(new StudyTagId());
            studyTag.setStudy(study);
            studyTag.setTag(tag);

            studyTagRepository.save(studyTag);
        }
    }

    @Transactional
    public StudyJoinRequestDTO requestToJoinStudy(Long studyId, String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (studyJoinRequestRepository.existsById(new StudyJoinRequestId(member.getId(), study.getId()))) throw new StudyJoinRequestAlreadyExistsException("이미 가입 신청한 스터디입니다.");
        if (!study.isRecruit()) throw new StudyNotRecruitException("회원 모집이 종료된 스터디입니다.");
        if (!getRole(loginId, study).equals("guest")) throw new StudyAccessDeniedException("이미 가입된 회원입니다.");

        StudyJoinRequest joinRequest = new StudyJoinRequest();
        joinRequest.setId(new StudyJoinRequestId());
        joinRequest.setMember(member);
        joinRequest.setStudy(study);

        return studyMapper.toJoinRequestDTO(member, studyJoinRequestRepository.save(joinRequest).getRequestDate());
    }

    @Transactional
    public void acceptJoinRequest(Long studyId, String loginId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId) && (study.getSubManager() == null || !study.getSubManager().getLoginId().equals(loginId))) {
            throw new StudyAccessDeniedException("권한이 없습니다.");
        }

        StudyMember acceptedMember = new StudyMember();
        acceptedMember.setId(new StudyMemberId());
        acceptedMember.setMember(member);
        acceptedMember.setStudy(study);

        studyMemberRepository.save(acceptedMember);
        studyJoinRequestRepository.deleteById(new StudyJoinRequestId(memberId, studyId));
    }

    public Page<StudySummaryDTO> getStudies(Pageable pageable, String sortBy, String keyword, String recruit) {
        Page<Study> studies;
        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasRecruit = recruit != null && !recruit.isBlank();
        boolean isRecruit = "open".equalsIgnoreCase(recruit);

        if (hasRecruit && ("hot".equals(sortBy) || hasKeyword)) {
            studies = studyRepository.findByKeywordAndRecruit(pageable, sortBy, keyword, isRecruit);
        } else if (hasKeyword) {
            studies = studyRepository.findByKeyword(pageable, sortBy, keyword);
        } else if (hasRecruit) {
            studies = studyRepository.findByIsRecruit(isRecruit, pageable);
        } else {
            if ("hot".equals(sortBy)) {
                studies = studyRepository.findAllOrderByMemberCount(pageable);
            } else {
                studies = studyRepository.findAll(pageable);
            }
        }

        return studies.map(studyMapper::toSummaryDTO);
    }

    public StudyMyDTO getMyStudies(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        List<StudySummaryDTO> creates = studyRepository.findByManagerIdOrderByIdDesc(member.getId()).stream()
                .map(studyMapper::toSummaryDTO)
                .collect(Collectors.toList());
        List<StudySummaryDTO> joins = studyMemberRepository.findStudiesByMemberId(member.getId()).stream()
                .map(studyMapper::toSummaryDTO)
                .collect(Collectors.toList());
        joins.removeAll(creates);

        return studyMapper.toMyDTO(creates, joins);
    }

    public StudyDetailDTO getStudy(Long studyId, String loginId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));

        String role = getRole(loginId, study);
        long memberCount = studyMemberRepository.countByStudyId(studyId);

        return studyMapper.toDetailDTO(study, memberCount, role);
    }

    public List<StudyJoinRequestDTO> getJoinRequests(Long studyId, String loginId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId) && (study.getSubManager() == null || !study.getSubManager().getLoginId().equals(loginId))) {
            throw new StudyAccessDeniedException("권한이 없습니다.");
        }

        List<StudyJoinRequest> joinRequests = studyJoinRequestRepository.findByStudyIdOrderByRequestDateDesc(studyId);

        return joinRequests.stream()
                .map(request -> studyMapper.toJoinRequestDTO(request.getMember(), request.getRequestDate()))
                .collect(Collectors.toList());
    }

    public List<StudyMemberDTO> getStudyMembers(Long studyId, String loginId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        if (getRole(loginId, study).equals("guest")) throw new StudyAccessDeniedException("스터디의 회원이 아닙니다.");

        List<StudyMember> members = studyMemberRepository.findByStudyIdOrdered(studyId);

        return members.stream()
                .map(studyMapper::toMemberDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delegateManager(Long studyId, String loginId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId)) throw new StudyAccessDeniedException("권한이 없습니다.");
        if (study.getSubManager() == null) throw new StudySubManagerNotFoundException("스터디에 부관리자가 존재하지 않습니다.");

        Member manager = study.getManager();
        Member subManager = study.getSubManager();

        study.setManager(subManager);
        study.setSubManager(manager);
        studyRepository.save(study);
    }

    @Transactional
    public void updateRecruitmentStatus(Long studyId, String loginId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId) && (study.getSubManager() == null || !study.getSubManager().getLoginId().equals(loginId))) {
            throw new StudyAccessDeniedException("권한이 없습니다.");
        }

        study.setRecruit(!study.isRecruit());
        studyRepository.save(study);
    }

    @Transactional
    public StudySubMangerDTO setSubManager(Long studyId, String loginId, Long memberId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        if (!study.getManager().getLoginId().equals(loginId)) throw new StudyAccessDeniedException("권한이 없습니다.");
        if (study.getSubManager() != null) throw new StudySubMangerAlreadyExistsException("스터디에 이미 부관리자가 존재합니다.");

        if (getRole(loginId, study).equals("guest")) throw new StudyAccessDeniedException("스터디의 회원이 아닙니다.");

        study.setSubManager(member);
        studyRepository.save(study);

        return studyMapper.toSubManagerDTO(member);
    }

    @Transactional
    public void declineJoinRequest(Long studyId, String loginId, Long memberId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId) && (study.getSubManager() == null || !study.getSubManager().getLoginId().equals(loginId))) {
            throw new StudyAccessDeniedException("권한이 없습니다.");
        }

        studyJoinRequestRepository.deleteById(new StudyJoinRequestId(memberId, studyId));
    }

    @Transactional
    public void deleteStudy(Long studyId, String loginId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId)) throw new StudyAccessDeniedException("권한이 없습니다.");
        if (studyMemberRepository.countByStudyId(studyId) > 1L) throw new StudyHasOtherMembersException("스터디에 다른 회원이 존재합니다.");

        imageService.delete(study.getThumbnailImage());
        studyRepository.deleteById(studyId);
    }

    @Transactional
    public void kickAll(Long studyId, String loginId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId)) throw new StudyAccessDeniedException("권한이 없습니다.");

        study.setSubManager(null);
        studyRepository.save(study);
        studyMemberRepository.deleteAllByStudyIdAndMemberIdNot(studyId, study.getManager().getId());
    }

    @Transactional
    public void kickMember(Long studyId, String loginId, Long memberId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (getRole(loginId, study).equals("guest")) throw new StudyAccessDeniedException("스터디의 회원이 아닙니다.");
        // 총관리자도 아니고, 부관리자도 아닐 경우
        if (!study.getManager().getLoginId().equals(loginId) && (study.getSubManager() == null || !study.getSubManager().getLoginId().equals(loginId))) {
            throw new StudyAccessDeniedException("권한이 없습니다.");
        }
        // 부관리자가 총관리자를 추방할 경우
        if (study.getSubManager() != null && study.getSubManager().getLoginId().equals(loginId) && study.getManager().getId().equals(memberId)) {
            throw new StudyAccessDeniedException("권한이 없습니다.");
        }

        if (study.getSubManager() != null && study.getSubManager().getId().equals(memberId)) {
            study.setSubManager(null);
            studyRepository.save(study);
        }

        studyMemberRepository.deleteById(new StudyMemberId(memberId, studyId));
    }

    @Transactional
    public void leaveStudy(Long studyId, String loginId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        if (getRole(loginId, study).equals("guest")) throw new StudyAccessDeniedException("스터디의 회원이 아닙니다.");

        if (study.getSubManager() != null && study.getSubManager().getNickname().equals(member.getNickname())) {
            study.setSubManager(null);
            studyRepository.save(study);
        }

        StudyMember studyMember = studyMemberRepository.findByMemberIdAndStudyId(member.getId(), studyId);
        studyMemberRepository.deleteById(studyMember.getId());
    }

    @Transactional
    public void unsetSubManager(Long studyId, String loginId) {
        memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));
        if (!study.getManager().getLoginId().equals(loginId)) throw new StudyAccessDeniedException("권한이 없습니다.");
        if (study.getSubManager() == null) throw new StudySubManagerNotFoundException("스터디에 부관리자가 존재하지 않습니다.");

        study.setSubManager(null);
        studyRepository.save(study);
    }

    private String getRole(String loginId, Study study) {
        if (studyMemberRepository.existsByStudyIdAndMemberLoginId(study.getId(), loginId)) {
            if (loginId.equals(study.getManager().getLoginId())) return "manager";
            else if (study.getSubManager() != null && loginId.equals(study.getSubManager().getLoginId())) return "subManager";
            else return "member";
        } else {
            return "guest";
        }
    }

}
