package com.pavelshapel.starter.boot.spring.zone;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static org.springframework.util.StringUtils.hasText;

import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;

public final class ZoneRegistry {
  private static final String ETC_PREFIX = "Etc";
  private static final String SYSTEM_V_PREFIX = "SystemV";

  private final Map<String, Set<Zone>> zones;

  ZoneRegistry() {
    this.zones =
        ZoneId.getAvailableZoneIds().stream()
            .filter(isContainingSlash())
            .filter(isNotStartingWith(ETC_PREFIX))
            .filter(isNotStartingWith(SYSTEM_V_PREFIX))
            .map(Zone::new)
            .collect(
                groupingBy(
                    Zone::region,
                    TreeMap::new,
                    collectingAndThen(
                        toCollection(TreeSet::new), Collections::unmodifiableNavigableSet)));
  }

  public TreeSet<Zone> getZoneIdsByRegion(String region) {
    return new TreeSet<>(zones.getOrDefault(region, emptySet()));
  }

  public TreeSet<String> searchZoneIds(String query) {
    if (!hasText(query)) {
      return new TreeSet<>();
    }
    return zones.values().stream()
        .flatMap(Collection::stream)
        .map(Zone::id)
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
