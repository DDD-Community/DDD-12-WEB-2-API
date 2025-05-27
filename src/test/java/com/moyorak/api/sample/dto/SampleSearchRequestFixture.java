package com.moyorak.api.sample.dto;

public class SampleSearchRequestFixture {

    public static SampleSearchRequest fixture(final String title, final String content) {
        return new SampleSearchRequest(title, content);
    }
}
