package com.moyorak.api.sample.dto;

public class SampleUpdateRequestFixture {

    public static SampleUpdateRequest fixture(final String title, final String content) {
        return new SampleUpdateRequest(title, content);
    }
}
