package com.socex.social_extractor.domain.shared;


import com.socex.social_extractor.domain.exception.DomainValidationException;
import org.apache.commons.lang3.StringUtils;

public final class DomainValidator {

    public static void notNullOrBlank(String value, String fieldName) {
        if (StringUtils.isBlank(value)) {
            throw new DomainValidationException(fieldName + " cannot be null or blank");
        }
    }

    public static <T> void notNull(T value, String fieldName) {
        if (value == null) {
            throw new DomainValidationException(fieldName + " cannot be null");
        };
    }
}
