package com.moyorak.api.history.service;

import com.moyorak.api.history.domain.SearchHistory;
import com.moyorak.api.history.repository.SearchHistoryRepository;
import com.moyorak.api.team.dto.TeamRestaurantSearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class HistoryEventHandler {

    private final SearchHistoryRepository searchHistoryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSearchEvent(final TeamRestaurantSearchEvent event) {
        final SearchHistory searchHistory =
                SearchHistory.create(event.keyword(), event.teamId(), event.userId());
        searchHistoryRepository.save(searchHistory);
    }
}
