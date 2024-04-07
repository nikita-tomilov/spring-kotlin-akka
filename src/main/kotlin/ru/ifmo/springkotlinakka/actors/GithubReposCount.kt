package ru.ifmo.springkotlinakka.actors

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Status
import mu.KLogging
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.ifmo.springkotlinakka.config.AkkaExtension
import ru.ifmo.springkotlinakka.messages.*

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class GithubReposCount : AbstractActor() {

  private val awaitingExternalRequestBehavior = receiveBuilder()
      .match(GithubReposCountRequest::class.java, ::receivedExternalRequest)
      .matchAny { unknownMessage(it) }
      .build()

  private val awaitingGithubResponseBehavior = receiveBuilder()
      .match(GithubReposResponse::class.java, ::receivedGithubResponse)
      .match(Status.Success::class.java) { logger.debug { "Got success status" } }
      .matchAny { unknownMessage(it) }
      .build()

  override fun createReceive(): Receive {
    return awaitingExternalRequestBehavior
  }

  private lateinit var originalRequestSender: ActorRef

  private fun receivedExternalRequest(request: GithubReposCountRequest) {
    originalRequestSender = sender
    val githubReposRequest = GithubReposRequest(request.username)
    // val actorRef = context.actorSelection("/user/akkaGithubClient")
    val actorRef = context.actorOf(AkkaExtension.EXT_PROV.get(context.system)!!
        .props("akkaGithubClient")) // real name: akka://spring-kotlin-akka/user/$a/$a
    actorRef.tell(githubReposRequest, self)
    context.become(awaitingGithubResponseBehavior)
  }

  private fun receivedGithubResponse(response: GithubReposResponse) {
    val originalResponse = GithubReposCountResponse(response.repos.size)
    originalRequestSender.tell(originalResponse, self)
    context.become(awaitingGithubResponseBehavior)
  }

  private fun unknownMessage(msg: Any) {
    logger.warn { "Unknown message $msg" }
    unhandled(msg)
  }

  companion object : KLogging()
}