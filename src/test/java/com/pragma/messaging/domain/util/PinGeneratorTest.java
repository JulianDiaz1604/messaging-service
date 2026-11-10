package com.pragma.messaging.domain.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PinGeneratorTest {

    @Test
    void generateProducesSixCharacterNumericPin() {
        for (int i = 0; i < 50; i++) {
            String pin = PinGenerator.generate();
            assertThat(pin).matches("\\d{6}");
        }
    }

    @Test
    void formatPadsWithLeadingZerosForMinimumValue() {
        assertThat(PinGenerator.format(0)).isEqualTo("000000");
    }

    @Test
    void formatPadsWithLeadingZerosForSmallValue() {
        assertThat(PinGenerator.format(1)).isEqualTo("000001");
        assertThat(PinGenerator.format(42)).isEqualTo("000042");
        assertThat(PinGenerator.format(1234)).isEqualTo("001234");
    }

    @Test
    void formatDoesNotPadMaximumSixDigitValue() {
        assertThat(PinGenerator.format(999999)).isEqualTo("999999");
    }

    @Test
    void generateProducesPinWithExactlyPinLengthCharacters() {
        String pin = PinGenerator.generate();
        assertThat(pin).hasSize(DomainConstants.PIN_LENGTH);
    }
}
