package ru.ifmo.springkotlinakka.messagequeue

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.apache.kafka.common.serialization.IntegerSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*

// docker run -p 9092:9092 bashj79/kafka-kraft
// @Configuration
// @EnableKafka
class KafkaConfig {

  companion object {
    const val topicName = "kafkaTopic"
    const val key = 42
  }

  @Bean
  fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<Int, String>):
      ConcurrentKafkaListenerContainerFactory<Int, String> {
    val factory = ConcurrentKafkaListenerContainerFactory<Int, String>()
    factory.consumerFactory = consumerFactory
    return factory
  }

  @Bean
  fun kafkaTemplate(producerFactory: ProducerFactory<Int, String>) = KafkaTemplate(producerFactory)

  @Bean
  fun consumerFactory() = DefaultKafkaConsumerFactory<Int, String>(
      mapOf(
          ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
          ConsumerConfig.GROUP_ID_CONFIG to "group",
          ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to IntegerDeserializer::class.java,
          ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
          ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest"
      )
  )

  @Bean
  fun kafkaReceiver() = KafkaReceiver()

  @Bean
  fun producerFactory() = DefaultKafkaProducerFactory<Int, String>(
      mapOf(
          ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to "localhost:9092",
          ProducerConfig.LINGER_MS_CONFIG to 10,
          ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to IntegerSerializer::class.java,
          ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java
      )
  )

  @Bean
  fun kafkaSender(template: KafkaTemplate<Int, String>): KafkaSender {
    return KafkaSender(template)
  }
}