package com.pavelshapel.starter.boot.spring.timezone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class TimezoneTest {
  @Test
  void shouldParseRegionAndCity() {
    Timezone timezone = new Timezone(/* id= */ "Europe/Moscow");

    assertThat(timezone)
        .hasFieldOrPropertyWithValue("id", "Europe/Moscow")
        .hasFieldOrPropertyWithValue("region", "Europe")
        .hasFieldOrPropertyWithValue("city", "Moscow");
  }

  @Test
  void shouldSortByRegionThenCity() {
    Timezone tokyo = new Timezone(/* id= */ "Asia/Tokyo");
    Timezone zurich = new Timezone(/* id= */ "Europe/Zurich");
    Timezone amsterdam = new Timezone(/* id= */ "Europe/Amsterdam");

    // Region comparison: Asia < Europe
    assertThat(tokyo.compareTo(zurich)).isLessThan(0);
    // Within same region, city comparison: Amsterdam < Zurich
    assertThat(amsterdam.compareTo(zurich)).isLessThan(0);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldThrowWhenIdIsInvalid(String id) {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Timezone(/* id= */ id, /* region= */ "", /* city= */ ""),
        "Id must not be empty");
  }

  @Test
  void shouldParseRegionOnlyWhenNoSlash() {
    Timezone timezone = new Timezone(/* id= */ "UTC");

    assertThat(timezone)
        .hasFieldOrPropertyWithValue("id", "UTC")
        .hasFieldOrPropertyWithValue("region", "UTC")
        .hasFieldOrPropertyWithValue("city", null);
  }

  @Test
  void shouldParseMultipleSeparators() {
    Timezone timezone = new Timezone(/* id= */ "Europe/London/Extra");

    assertThat(timezone)
        .hasFieldOrPropertyWithValue("id", "Europe/London/Extra")
        .hasFieldOrPropertyWithValue("region", "Europe")
        .hasFieldOrPropertyWithValue("city", "London/Extra");
  }

  @Test
  void shouldCreateWithExplicitAllArguments() {
    Timezone timezone =
        new Timezone(/* id= */ "Asia/Tokyo", /* region= */ "Asia", /* city= */ "Tokyo");

    assertThat(timezone)
        .hasFieldOrPropertyWithValue("id", "Asia/Tokyo")
        .hasFieldOrPropertyWithValue("region", "Asia")
        .hasFieldOrPropertyWithValue("city", "Tokyo");
  }

  @Test
  void shouldReturnZeroForEqualZones() {
    Timezone timezone1 = new Timezone(/* id= */ "Europe/Moscow");
    Timezone timezone2 = new Timezone(/* id= */ "Europe/Moscow");

    assertThat(timezone1.compareTo(timezone2)).isZero();
  }
}
