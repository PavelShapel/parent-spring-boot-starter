package com.pavelshapel.amqp.spring.boot.starter.rabbit;

import com.pavelshapel.amqp.spring.boot.starter.StarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ContextConfiguration(initializers = MessageSenderTest.RabbitMQInitializer.class,
        classes = {
                StarterAutoConfiguration.class
        })
@Testcontainers
class MessageSenderTest {

    @Container
    private static final RabbitMQContainer RABBIT_MQ_CONTAINER =
            new RabbitMQContainer(DockerImageName.parse("rabbitmq:management"));

    @MockBean
    private MessageSender amqpMessageSender;

    @Test
    void init() {
        assertThat(RABBIT_MQ_CONTAINER).isNotNull();
        assertThat(amqpMessageSender).isNotNull();
    }

    @Test
    void send_WithValidParam_ShouldSendMessage() {
        amqpMessageSender.send(any());
        verify(amqpMessageSender, times(1)).send(any());
    }

    public static class RabbitMQInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.rabbitmq.host=" + RABBIT_MQ_CONTAINER.getHost(),
                    "spring.rabbitmq.port=" + RABBIT_MQ_CONTAINER.getMappedPort(5672),
                    "spring.rabbitmq.username=" + RABBIT_MQ_CONTAINER.getAdminUsername(),
                    "spring.rabbitmq.password=guest" + RABBIT_MQ_CONTAINER.getAdminPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}