package com.pavelshapel.starter.boot.spring.zone;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.TreeSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class ZoneRegistryTest {
  private final ZoneRegistry registry = new ZoneRegistry();

  @Test
  void shouldReturnEmptyForUnknownRegion() {
    TreeSet<Zone> result = registry.getZoneIdsByRegion("NO_SUCH_REGION");

    assertThat(result).isEmpty();
  }

  @Test
  void shouldContainKnownZoneForDerivedRegion() {
    TreeSet<Zone> result = registry.getZoneIdsByRegion("Europe");

    assertThat(result).isNotEmpty();
  }

  @ParameterizedTest
  @NullAndEmptySource
  void searchZoneIdsShouldReturnEmptyForNullOrEmptyQuery(String query) {
    TreeSet<String> result = registry.searchZoneIds(query);

    assertThat(result).isEmpty();
  }

  @Test
  void searchZoneIdsShouldFindMatchingIdsCaseInsensitive() {
    TreeSet<String> result = registry.searchZoneIds("war");

    assertThat(result).containsExactly("Europe/Warsaw");
  }
}
