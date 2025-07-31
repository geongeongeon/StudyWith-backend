package com.studywith.api.domain.study.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudyCreateDTO {

    private String thumbnailImage;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String region;

    private List<String> tags;

}
