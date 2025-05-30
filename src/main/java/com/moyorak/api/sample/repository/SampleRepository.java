package com.moyorak.api.sample.repository;

import com.moyorak.api.sample.domain.Sample;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SampleRepository extends CrudRepository<Sample, Long> {

    @Query(
            value =
                    """
            SELECT s FROM Sample s
            WHERE (:title IS NULL OR s.title LIKE CONCAT('%', :title, '%'))
            AND (:content IS NULL OR s.content LIKE CONCAT('%', :content, '%'))
            ORDER BY s.id DESC
        """)
    @QueryHints(
            @QueryHint(
                    name = "org.hibernate.comment",
                    value = "SampleRepository.findByConditions : 샘플의 데이터를 검색합니다."))
    Page<Sample> findByConditions(
            @Param("title") String title, @Param("content") String content, Pageable pageable);
}
