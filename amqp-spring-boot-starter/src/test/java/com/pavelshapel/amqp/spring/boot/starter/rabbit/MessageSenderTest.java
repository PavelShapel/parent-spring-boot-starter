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
@ContextConfiguration(initializers = MessageSenderTest.Initializer.class,
        classes = {
                StarterAutoConfiguration.class
        })
@Testcontainers
class MessageSenderTest {

    @Container
    private static final RabbitMQContainer rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:management"));

    @MockBean
    private MessageSender amqpMessageSender;

    @Test
    void init() {
        assertThat(rabbit).isNotNull();
        assertThat(amqpMessageSender).isNotNull();
    }

    @Test
    void send_WithValidParam_ShouldSendMessage() {
        amqpMessageSender.send(any());
        verify(amqpMessageSender, times(1)).send(any());
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.rabbitmq.host=" + rabbit.getHost(),
                    "spring.rabbitmq.port=" + rabbit.getMappedPort(5672),
                    "spring.rabbitmq.username=" + rabbit.getAdminUsername(),
                    "spring.rabbitmq.password=guest" + rabbit.getAdminPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}