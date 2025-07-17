package com.moyorak.api.history.repository;

import com.moyorak.api.history.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {}
