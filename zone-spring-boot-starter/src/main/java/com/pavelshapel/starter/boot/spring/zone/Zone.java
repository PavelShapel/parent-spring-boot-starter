package com.pavelshapel.starter.boot.spring.zone;

import static org.springframework.util.StringUtils.hasText;

import java.util.Comparator;
import org.jspecify.annotations.NonNull;

public record Zone(String id, String region, String city) implements Comparable<Zone> {
  public Zone {
    if (!hasText(id)) {
      throw new IllegalArgumentException("Id must not be empty");
    }
  }

  public Zone(String id) {
    this(id, getRegion(id), getCity(id));
  }

  @Override
  public int compareTo(@NonNull Zone other) {
    return Comparator.comparing(Zone::region).thenComparing(Zone::city).compare(this, other);
  }

  private static String getRegion(String id) {
    return splitId(id)[0];
  }

  private static String getCity(String id) {
    try {
      return splitId(id)[1];
    } catch (Exception exception) {
      return null;
    }
  }

  private static String[] splitId(String id) {
    return id.split(/* regex= */ "/", /* limit= */ 2);
  }
}
