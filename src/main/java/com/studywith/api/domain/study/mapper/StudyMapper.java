package com.studywith.api.domain.study.mapper;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.study.dto.*;
import com.studywith.api.domain.study.entity.Study;
import com.studywith.api.domain.study.entity.StudyMember;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class StudyMapper {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Study toCreateEntity(StudyCreateDTO studyCreateDTO, Member member) {
        return Study.builder()
                .title(studyCreateDTO.getTitle())
                .description(studyCreateDTO.getDescription())
                .region(studyCreateDTO.getRegion())
                .thumbnailImage(studyCreateDTO.getThumbnailImage())
                .isRecruit(true)
                .isActive(true)
                .manager(member)
                .subManager(null)
                .build();
    }

    public StudyCreateDTO toCreateDTO(Study study) {
        List<String> tags = study.getTags().stream()
                .map(tag -> tag.getTag().getName())
                .toList();

        return StudyCreateDTO.builder()
                .thumbnailImage(study.getThumbnailImage())
                .title(study.getTitle())
                .description(study.getDescription())
                .region(study.getRegion())
                .tags(tags)
                .build();
    }

    public StudySummaryDTO toSummaryDTO(Study study) {
        List<String> tags = study.getTags().stream()
                .map(tag -> tag.getTag().getName())
                .toList();

        return StudySummaryDTO.builder()
                .id(study.getId())
                .title(study.getTitle())
                .description(study.getDescription())
                .thumbnailImage(study.getThumbnailImage())
                .region(study.getRegion())
                .tags(tags)
                .isRecruit(study.isRecruit())
                .build();
    }

    public StudyDetailDTO toDetailDTO(Study study, Long memberCount, String role) {
        List<String> tags = study.getTags().stream()
                .map(tag -> tag.getTag().getName())
                .toList();

        return StudyDetailDTO.builder()
                .manager(study.getManager().getNickname())
                .subManager(study.getSubManager() != null ? study.getSubManager().getNickname() : "-")
                .title(study.getTitle())
                .description(study.getDescription())
                .thumbnailImage(study.getThumbnailImage())
                .region(study.getRegion())
                .tags(tags)
                .isRecruit(study.isRecruit())
                .memberCount(memberCount)
                .createDate(study.getCreateDate().format(dateTimeFormatter))
                .modifyDate(study.getModifyDate() != null ? study.getModifyDate().format(dateTimeFormatter) : study.getCreateDate().format(dateTimeFormatter))
                .myRole(role)
                .build();
    }

    public StudyMyDTO toMyDTO(List<StudySummaryDTO> createList, List<StudySummaryDTO> joinList) {
        return StudyMyDTO.builder()
                .createList(createList)
                .joinList(joinList)
                .build();
    }

    public StudyJoinRequestDTO toJoinRequestDTO(Member member, LocalDateTime requestDate) {
        return StudyJoinRequestDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .gender(member.getGender().getValue())
                .birth(member.getBirth().format(dateFormatter))
                .region(member.getRegion())
                .profileImage(member.getProfileImage())
                .bio(member.getBio())
                .requestDate(requestDate.format(dateTimeFormatter))
                .build();
    }

    public StudyMemberDTO toMemberDTO(StudyMember studyMember) {
        return StudyMemberDTO.builder()
                .memberId(studyMember.getMember().getId())
                .nickname(studyMember.getMember().getNickname())
                .gender(studyMember.getMember().getGender().getValue())
                .birth(studyMember.getMember().getBirth().format(dateFormatter))
                .region(studyMember.getMember().getRegion())
                .profileImage(studyMember.getMember().getProfileImage())
                .bio(studyMember.getMember().getBio())
                .joinDate(studyMember.getJoinDate().format(dateTimeFormatter))
                .build();
    }

    public StudySubMangerDTO toSubManagerDTO(Member member) {
        return StudySubMangerDTO.builder()
                .subManager(member.getNickname())
                .build();
    }

}
