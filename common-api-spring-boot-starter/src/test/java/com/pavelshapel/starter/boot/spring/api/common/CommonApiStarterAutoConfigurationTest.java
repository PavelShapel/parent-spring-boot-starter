package com.pavelshapel.starter.boot.spring.api.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

final class CommonApiStarterAutoConfigurationTest {

  private final ApplicationContextRunner contextRunner =
      new ApplicationContextRunner()
          .withConfiguration(AutoConfigurations.of(CommonApiStarterAutoConfiguration.class));

  @Test
  void registersRuntimeEnvironmentWithDefaultProfilesWhenNoneActive() {
    contextRunner.run(context ->
        assertThat(context)
            .hasSingleBean(RuntimeEnvironment.class)
            .getBean(RuntimeEnvironment.class)
            .extracting(
                RuntimeEnvironment::activeProfiles,
                InstanceOfAssertFactories.collection(String.class))
            .containsExactly("default"));
  }

  @Test
  void registersRuntimeEnvironmentWithActiveProfiles() {
    contextRunner
        .withPropertyValues("spring.profiles.active=prod,cloud")
        .run(context ->
            assertThat(context)
                .hasSingleBean(RuntimeEnvironment.class)
                .getBean(RuntimeEnvironment.class)
                .extracting(
                    RuntimeEnvironment::activeProfiles,
                    InstanceOfAssertFactories.collection(String.class))
                .containsExactlyInAnyOrder("prod", "cloud"));
  }

  @Test
  void backsOffWhenCustomRuntimeEnvironmentBeanIsPresent() {
    var custom = new RuntimeEnvironment(Set.of("custom"));

    contextRunner
        .withBean(RuntimeEnvironment.class, () -> custom)
        .run(context ->
            assertThat(context)
                .hasSingleBean(RuntimeEnvironment.class)
                .getBean(RuntimeEnvironment.class)
                .isSameAs(custom));
  }
}
