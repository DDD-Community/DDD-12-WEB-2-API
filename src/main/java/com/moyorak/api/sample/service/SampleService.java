package com.moyorak.api.sample.service;

import com.moyorak.api.config.exception.domain.DataNotFoundException;
import com.moyorak.api.global.domain.ListResponse;
import com.moyorak.api.sample.domain.Sample;
import com.moyorak.api.sample.dto.SampleResponse;
import com.moyorak.api.sample.dto.SampleSaveRequest;
import com.moyorak.api.sample.dto.SampleSearchRequest;
import com.moyorak.api.sample.dto.SampleUpdateRequest;
import com.moyorak.api.sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    @Transactional(readOnly = true)
    public SampleResponse getDetail(final Long id) {
        final Sample sample = findByIdOrThrow(id);

        return SampleResponse.from(sample);
    }

    @Transactional(readOnly = true)
    public ListResponse<SampleResponse> search(final SampleSearchRequest request) {
        final Page<Sample> samples =
                sampleRepository.findByConditions(
                        request.getTitle(), request.getContent(), request.toPageable());

        return ListResponse.from(samples, SampleResponse::from);
    }

    @Transactional
    public void register(final SampleSaveRequest request) {
        sampleRepository.save(request.toEntity());
    }

    @Transactional
    public void modify(final Long id, final SampleUpdateRequest request) {
        Sample sample = findByIdOrThrow(id);

        sample.modify(request.title(), request.content());
    }

    @Transactional
    public void remove(final Long id) {
        Sample sample = findByIdOrThrow(id);

        sample.remove();

        sampleRepository.delete(sample);
    }

    private Sample findByIdOrThrow(final Long id) {
        return sampleRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }
}
