package com.moyorak.api.helper;

import java.util.Random;

public class TestFixtureUtils {
    private static final Random random = new Random();
    private static final String CHARACTERS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static Long createLongWithSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("크기는 음수일 수 없습니다.");
        }

        return (long) (random.nextDouble() * (size + 1));
    }

    public static String createStringWithLength(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("크기는 음수일 수 없습니다.");
        }

        if (size > 1024) {
            throw new IllegalArgumentException("크기는 1024보다 작아야 합니다.");
        }

        StringBuilder sb = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
