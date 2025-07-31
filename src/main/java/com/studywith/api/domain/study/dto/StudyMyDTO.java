package com.studywith.api.domain.study.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudyMyDTO {

    private List<StudySummaryDTO> createList;

    private List<StudySummaryDTO> joinList;

}
