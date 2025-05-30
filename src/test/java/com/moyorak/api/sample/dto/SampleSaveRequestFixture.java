package com.moyorak.api.sample.dto;

public class SampleSaveRequestFixture {

    public static SampleSaveRequest fixture(final String title, final String content) {
        return new SampleSaveRequest(title, content);
    }
}
