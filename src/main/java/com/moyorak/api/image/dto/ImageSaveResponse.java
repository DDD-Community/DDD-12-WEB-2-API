package com.moyorak.api.image.dto;

public record ImageSaveResponse(String url, String path) {

    public static ImageSaveResponse from(final String url, final String path) {
        return new ImageSaveResponse(url, path);
    }
}
