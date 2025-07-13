package com.moyorak.api.review.dto;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public record PhotoPath(String path) {
    public static List<PhotoPath> fromStrings(final List<String> paths) {
        return paths.stream().map(PhotoPath::new).toList();
    }

    public static Page<PhotoPath> toPage(final List<String> rawPaths, final Pageable pageable) {
        final List<PhotoPath> photoPaths = fromStrings(rawPaths);
        return new PageImpl<>(photoPaths, pageable, photoPaths.size());
    }
}
