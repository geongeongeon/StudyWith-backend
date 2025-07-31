package com.studywith.api.domain.tag.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tag")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
