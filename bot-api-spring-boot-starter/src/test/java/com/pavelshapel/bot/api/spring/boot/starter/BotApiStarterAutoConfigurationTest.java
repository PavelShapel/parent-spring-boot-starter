package com.pavelshapel.bot.api.spring.boot.starter;

import static org.assertj.core.api.Assertions.assertThat;

import com.pavelshapel.bot.api.spring.boot.starter.model.context.WorkerContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@SpringBootTest(
    classes = {
      BotApiStarterAutoConfiguration.class,
      BotApiStarterAutoConfigurationTest.ApplicableTestWorker1.class,
      BotApiStarterAutoConfigurationTest.NotApplicableTestWorker2.class,
      BotApiStarterAutoConfigurationTest.ApplicableTestWorker3.class
    })
class BotApiStarterAutoConfigurationTest {
  abstract static class TestWorker extends Worker {
    private final int order;
    private final boolean isApplicable;

    TestWorker(int order, boolean isApplicable) {
      this.order = order;
      this.isApplicable = isApplicable;
    }

    @Override
    public Void apply(WorkerContext payload) {
      return null;
    }

    @Override
    protected boolean isApplicable(WorkerContext payload) {
      return isApplicable;
    }

    @Override
    public int getOrder() {
      return order;
    }
  }

  @Component
  static class ApplicableTestWorker1 extends TestWorker {
    ApplicableTestWorker1() {
      super(/* order= */ 1, /* isApplicable= */ true);
    }
  }

  @Component
  static class NotApplicableTestWorker2 extends TestWorker {
    NotApplicableTestWorker2() {
      super(/* order= */ 2, /* isApplicable= */ false);
    }
  }

  @Component
  static class ApplicableTestWorker3 extends TestWorker {
    ApplicableTestWorker3() {
      super(/* order= */ 3, /* isApplicable= */ true);
    }
  }

  @Autowired private ApplicationContext applicationContext;

  @Test
  void shouldLoadWorkersProcessorBeanIntoContext() {
    assertThat(applicationContext.containsBean("workersProcessor")).isTrue();
  }

  @Test
  void shouldReturnWorkersProcessorInstance() {
    WorkersProcessor workersProcessor = applicationContext.getBean(WorkersProcessor.class);

    assertThat(workersProcessor).isInstanceOf(WorkersProcessor.class);
  }

  @Test
  void shouldCreateSingletonWorkersProcessorBean() {
    WorkersProcessor workersProcessorOne = applicationContext.getBean(WorkersProcessor.class);
    WorkersProcessor workersProcessorTwo = applicationContext.getBean(WorkersProcessor.class);

    assertThat(workersProcessorOne).isSameAs(workersProcessorTwo);
  }

  @Test
  void shouldHaveCacheManagerBeanWithSingletonScope() {
    assertThat(applicationContext.isSingleton("workersProcessor")).isTrue();
  }
}
