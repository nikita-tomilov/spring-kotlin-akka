package ru.ifmo.springkotlinakka.controllers

import akka.actor.ActorSystem
import akka.pattern.Patterns.ask
import akka.util.Timeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.springkotlinakka.config.AkkaExtension.Companion.EXT_PROV
import ru.ifmo.springkotlinakka.messages.TextMessage
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

@RestController
class TextMessageController(
    @Autowired private val system: ActorSystem
) {

  @GetMapping("/invert")
  fun invertHelp(): String {
    return "Please use /invert/{message} to fetch inverted message, example: /invert/123"
  }

  @GetMapping("/invert/{message}")
  fun invert(@PathVariable message: String): String {
    val actorRef = system.actorOf(EXT_PROV.get(system)!!.props("textMessageInverter"))

    val duration = FiniteDuration.create(1, TimeUnit.SECONDS)
    val timeout: Timeout = Timeout.durationToTimeout(duration)

    val future = ask(actorRef, TextMessage(message), timeout)
    val result = Await.result(future, duration)

    val resultMsg = result as TextMessage
    return resultMsg.payload
  }
}