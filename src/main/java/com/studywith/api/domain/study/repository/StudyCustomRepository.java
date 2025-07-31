package com.studywith.api.domain.study.repository;

import com.studywith.api.domain.study.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudyCustomRepository {

    Page<Study> findByKeyword(Pageable pageable, String sortBy, String keyword);

    Page<Study> findByKeywordAndRecruit(Pageable pageable, String sortBy, String keyword, boolean isRecruit);

}
