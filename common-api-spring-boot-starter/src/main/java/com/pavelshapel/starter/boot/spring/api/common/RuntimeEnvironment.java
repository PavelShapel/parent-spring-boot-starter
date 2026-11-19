package com.pavelshapel.starter.boot.spring.api.common;

import java.util.Set;

public record RuntimeEnvironment(Set<String> activeProfiles) {

  public RuntimeEnvironment {
    activeProfiles = Set.copyOf(activeProfiles);
  }
}
