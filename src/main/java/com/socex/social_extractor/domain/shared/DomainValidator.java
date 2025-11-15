package com.socex.social_extractor.domain.shared;


import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public final class DomainValidator {

    public static void notNullOrBlank(String value, String fieldName) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException(fieldName + " cannot be null or blank");
        }
    }

    public static <T> void notNull(T value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null");
    }
}
