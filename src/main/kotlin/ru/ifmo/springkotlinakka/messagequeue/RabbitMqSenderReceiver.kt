package ru.ifmo.springkotlinakka.messagequeue

import mu.KLogging
import org.springframework.amqp.core.Message
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RabbitMqSender(
  private val rabbitTemplate: RabbitTemplate
) {

  @Scheduled(fixedDelay = 3000)
  fun sendViaRabbit() {
    val msg = Message("Hello from RabbitMQ!".toByteArray())
    rabbitTemplate.send(RabbitMqConfig.topicExchangeName, "rmq.routingKey", msg)
    logger.warn { "Sent message '${String(msg.body)}'" }
  }

  companion object : KLogging()
}

@Component
class RabbitMqReceiver {

  @Suppress("unused") // Used by RabbitMQ
  fun receiveMessage(message: ByteArray) {
    logger.warn { "Received message '${String(message)}'" }
  }

  companion object : KLogging()
}