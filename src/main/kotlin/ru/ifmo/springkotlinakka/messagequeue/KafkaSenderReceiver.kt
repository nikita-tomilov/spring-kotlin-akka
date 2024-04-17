package ru.ifmo.springkotlinakka.messagequeue

import mu.KLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.ifmo.springkotlinakka.messagequeue.KafkaConfig.Companion.key
import ru.ifmo.springkotlinakka.messagequeue.KafkaConfig.Companion.topicName

// @Component
class KafkaSender(private val template: KafkaTemplate<Int, String>) {

  companion object : KLogging()

  @Scheduled(fixedDelay = 3000)
  fun sendViaKafka() {
    val msg = "Hello from Kafka!"
    template.send(topicName, key, msg)
    logger.warn { "Sent message '$msg'" }
  }
}

// @Component
class KafkaReceiver {

  companion object : KLogging()

  @KafkaListener(id = "listener", topics = [topicName])
  fun listener(message: String) {
    logger.warn { "Received message '$message'" }
  }

}