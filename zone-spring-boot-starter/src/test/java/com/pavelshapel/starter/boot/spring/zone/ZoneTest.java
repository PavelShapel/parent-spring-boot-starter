package com.pavelshapel.starter.boot.spring.zone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ZoneTest {
  @Test
  void shouldParseRegionAndCity() {
    Zone zone = new Zone(/* id= */ "Europe/Moscow");

    assertThat(zone)
        .hasFieldOrPropertyWithValue("id", "Europe/Moscow")
        .hasFieldOrPropertyWithValue("region", "Europe")
        .hasFieldOrPropertyWithValue("city", "Moscow");
  }

  @Test
  void shouldSortByRegionThenCity() {
    Zone tokyo = new Zone(/* id= */ "Asia/Tokyo");
    Zone zurich = new Zone(/* id= */ "Europe/Zurich");
    Zone amsterdam = new Zone(/* id= */ "Europe/Amsterdam");

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
        () -> new Zone(/* id= */ "", /* region= */ "", /* city= */ ""),
        "Id must not be empty");
  }

  @Test
  void shouldParseRegionOnlyWhenNoSlash() {
    Zone zone = new Zone(/* id= */ "UTC");

    assertThat(zone)
        .hasFieldOrPropertyWithValue("id", "UTC")
        .hasFieldOrPropertyWithValue("region", "UTC")
        .hasFieldOrPropertyWithValue("city", null);
  }

  @Test
  void shouldParseMultipleSeparators() {
    Zone zone = new Zone(/* id= */ "Europe/London/Extra");

    assertThat(zone)
        .hasFieldOrPropertyWithValue("id", "Europe/London/Extra")
        .hasFieldOrPropertyWithValue("region", "Europe")
        .hasFieldOrPropertyWithValue("city", "London/Extra");
  }

  @Test
  void shouldCreateWithExplicitAllArguments() {
    Zone zone = new Zone(/* id= */ "Asia/Tokyo", /* region= */ "Asia", /* city= */ "Tokyo");

    assertThat(zone)
        .hasFieldOrPropertyWithValue("id", "Asia/Tokyo")
        .hasFieldOrPropertyWithValue("region", "Asia")
        .hasFieldOrPropertyWithValue("city", "Tokyo");
  }

  @Test
  void shouldReturnZeroForEqualZones() {
    Zone zone1 = new Zone(/* id= */ "Europe/Moscow");
    Zone zone2 = new Zone(/* id= */ "Europe/Moscow");

    assertThat(zone1.compareTo(zone2)).isZero();
  }
}
