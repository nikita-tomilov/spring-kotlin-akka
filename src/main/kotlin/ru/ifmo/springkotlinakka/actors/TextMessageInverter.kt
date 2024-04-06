package ru.ifmo.springkotlinakka.actors

import akka.actor.UntypedAbstractActor
import mu.KLogging
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.ifmo.springkotlinakka.model.TextMessage

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class TextMessageInverter : UntypedAbstractActor() {

  @Throws(Throwable::class)
  override fun onReceive(message: Any) {
    if (message is TextMessage) {
      val payload = message.payload
      val reversed = payload.reversed()
      logger.warn { "Someone asked us to invert string $payload, result is $reversed" }
      sender.tell(TextMessage(reversed), self)
    } else {
      logger.warn { "Unknown message $message" }
      unhandled(message)
    }
  }

  companion object : KLogging()
}