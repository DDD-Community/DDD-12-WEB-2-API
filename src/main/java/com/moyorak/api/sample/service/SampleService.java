package com.moyorak.api.sample.service;

import com.moyorak.api.sample.dto.SampleSaveRequest;
import com.moyorak.api.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    @Transactional
    public void register(final SampleSaveRequest request) {
        sampleRepository.save(request.toEntity());
    }
}
