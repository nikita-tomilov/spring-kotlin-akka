package ru.ifmo.springkotlinakka.controllers

import akka.actor.ActorSystem
import akka.pattern.Patterns
import akka.util.Timeout
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.springkotlinakka.config.AkkaExtension
import ru.ifmo.springkotlinakka.messages.*
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

@RestController
class GithubAkkaController(
  @Autowired private val system: ActorSystem
) {

  @GetMapping("/fetchAkka")
  fun fetchHelp(): String {
    return "Please use /fetchAkka/{username} to fetch repositories summary, example: /fetchAkka/avplatonov"
  }

  @GetMapping("/fetchAkka/{username}")
  fun fetchRepositoriesSummary(@PathVariable username: String): String {
    val actorRef = system.actorOf(AkkaExtension.EXT_PROV.get(system)!!.props("githubReposCount"))

    val duration = FiniteDuration.create(5, TimeUnit.SECONDS)
    val timeout: Timeout = Timeout.durationToTimeout(duration)

    val future = Patterns.ask(actorRef, GithubReposCountRequest(username), timeout)
    val result = Await.result(future, duration)

    val resultMsg = result as GithubReposCountResponse
    return "User $username has ${resultMsg.count} repositories\n"
  }
}