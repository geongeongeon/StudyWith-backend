package com.studywith.api.domain.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StudySummaryDTO {

    private Long id;

    private String title;

    private String description;

    private String thumbnailImage;

    private String region;

    private List<String> tags;

    private Boolean isRecruit;

}
