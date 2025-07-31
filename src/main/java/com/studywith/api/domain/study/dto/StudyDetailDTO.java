package com.studywith.api.domain.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudyDetailDTO {

    private String manager;

    private String subManager;

    private String title;

    private String description;

    private String thumbnailImage;

    private String region;

    private List<String> tags;

    private Boolean isRecruit;

    private Long memberCount;

    private String createDate;

    private String modifyDate;

    private String myRole; // 로그인한 사람의 권한

}
