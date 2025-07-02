package com.moyorak.api.image.dto;

public class ImageDeleteRequestFixture {

    public static ImageDeleteRequest fixture(final Long userId) {
        return ImageDeleteRequestFixture.create(userId, "");
    }

    public static ImageDeleteRequest create(final Long userId, final String path) {
        return new ImageDeleteRequest(userId, path);
    }
}
