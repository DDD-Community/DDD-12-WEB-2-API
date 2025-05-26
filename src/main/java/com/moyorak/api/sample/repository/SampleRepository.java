package com.moyorak.api.sample.repository;

import com.moyorak.api.sample.domain.Sample;
import org.springframework.data.repository.CrudRepository;

public interface SampleRepository extends CrudRepository<Sample, Long> {}
