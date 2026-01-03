package com.socex.social_extractor.domain.shared;

import com.socex.social_extractor.domain.exception.DomainValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DomainValidatorTest {

    @Test
    void notNull_ShouldPass_WhenValueIsNotNull() {
        assertThatCode(() -> DomainValidator.notNull(new Object(), "TestField"))
                .doesNotThrowAnyException();
    }

    @Test
    void notNull_ShouldThrowException_WhenValueIsNull() {
        assertThatThrownBy(() -> DomainValidator.notNull(null, "TestField"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("TestField cannot be null");
    }

    @Test
    void notNullOrBlank_ShouldPass_WhenValueIsValid() {
        assertThatCode(() -> DomainValidator.notNullOrBlank("valid string", "TestField"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n", " "})
    void notNullOrBlank_ShouldThrowException_WhenValueIsInvalid(String invalidInput) {
        assertThatThrownBy(() -> DomainValidator.notNullOrBlank(invalidInput, "TestField"))
                .isInstanceOf(DomainValidationException.class)
                .hasMessage("TestField cannot be null or blank");
    }
}