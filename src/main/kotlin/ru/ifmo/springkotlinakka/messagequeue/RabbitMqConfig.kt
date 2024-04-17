package ru.ifmo.springkotlinakka.messagequeue

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// docker run -p 5671:5671 -p 5672:5672 -p 15671:15671 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=password rabbitmq:3-management
// http://localhost:15672/#/
// @Configuration
class RabbitMqConfig {

  companion object {
    const val topicExchangeName: String = "rmq-exchange"
    const val queueName: String = "rmq-queue"
  }

  @Bean
  fun queue(): Queue {
    return Queue(queueName, false)
  }

  @Bean
  fun exchange(): TopicExchange {
    return TopicExchange(topicExchangeName)
  }

  @Bean
  fun binding(queue: Queue?, exchange: TopicExchange?): Binding {
    return BindingBuilder.bind(queue).to(exchange).with("rmq.#")
  }

  @Bean
  fun container(
    connectionFactory: ConnectionFactory,
    listenerAdapter: MessageListenerAdapter
  ): SimpleMessageListenerContainer {
    val container = SimpleMessageListenerContainer()
    container.connectionFactory = connectionFactory
    container.setQueueNames(queueName)
    container.setMessageListener(listenerAdapter)
    return container
  }

  @Bean
  fun connector(): CachingConnectionFactory {
    val connectionFactory = CachingConnectionFactory("localhost")
    connectionFactory.port = 5672
    connectionFactory.username = "user"
    connectionFactory.setPassword("password")
    return connectionFactory
  }

  @Bean
  fun listenerAdapter(receiver: RabbitMqReceiver): MessageListenerAdapter {
    return MessageListenerAdapter(receiver, "receiveMessage")
  }

}