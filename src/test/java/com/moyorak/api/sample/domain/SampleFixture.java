package com.moyorak.api.sample.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class SampleFixture {

    public static Sample fixture(final Long id, final String title, final String content) {
        Sample sample = new Sample();

        ReflectionTestUtils.setField(sample, "id", id);
        ReflectionTestUtils.setField(sample, "title", title);
        ReflectionTestUtils.setField(sample, "content", content);

        return sample;
    }
}
