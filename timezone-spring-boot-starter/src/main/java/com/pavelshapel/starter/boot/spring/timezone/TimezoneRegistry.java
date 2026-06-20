package com.pavelshapel.starter.boot.spring.timezone;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.util.StringUtils.hasText;

import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;

public final class TimezoneRegistry {
  private static final String ETC_PREFIX = "Etc";
  private static final String SYSTEM_V_PREFIX = "SystemV";

  private final Map<String, Set<Timezone>> timezones;

  TimezoneRegistry() {
    this.timezones =
        ZoneId.getAvailableZoneIds().stream()
            .filter(isContainingSlash())
            .filter(isNotStartingWith(ETC_PREFIX))
            .filter(isNotStartingWith(SYSTEM_V_PREFIX))
            .map(Timezone::new)
            .collect(
                groupingBy(
                    Timezone::region,
                    TreeMap::new,
                    collectingAndThen(
                        toCollection(TreeSet::new), Collections::unmodifiableNavigableSet)));
  }

  public TreeSet<String> getRegions() {
    return new TreeSet<>(timezones.keySet());
  }

  public TreeSet<Timezone> getZoneIdsByRegion(String region) {
    return new TreeSet<>(timezones.getOrDefault(region, emptySet()));
  }

  public TreeSet<String> searchZoneIds(String query) {
    if (!hasText(query)) {
      return new TreeSet<>();
    }
    return timezones.values().stream()
        .flatMap(Collection::stream)
        .map(Timezone::id)
        .filter(zoneId -> zoneId.toLowerCase().contains(query.toLowerCase()))
        .collect(toCollection(TreeSet::new));
  }

  private static Predicate<String> isContainingSlash() {
    return zoneId -> zoneId.contains("/");
  }

  private static Predicate<String> isNotStartingWith(String prefix) {
    return zoneId -> !zoneId.startsWith(prefix);
  }
}
