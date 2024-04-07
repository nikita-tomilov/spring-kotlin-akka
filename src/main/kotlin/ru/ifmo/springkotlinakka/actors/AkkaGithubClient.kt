package ru.ifmo.springkotlinakka.actors

import akka.actor.AbstractActor
import akka.actor.Status
import akka.http.javadsl.Http
import akka.http.javadsl.model.HttpRequest
import akka.http.javadsl.model.HttpResponse
import akka.http.javadsl.model.StatusCodes
import akka.stream.javadsl.Sink
import akka.stream.javadsl.Source
import akka.util.ByteString
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import mu.KLogging
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import ru.ifmo.springkotlinakka.messages.GithubReposRequest
import ru.ifmo.springkotlinakka.messages.GithubReposResponse
import ru.ifmo.springkotlinakka.model.GithubRepo

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class AkkaGithubClient : AbstractActor() {

  private val om = ObjectMapper().registerKotlinModule()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  private val behavior = receiveBuilder()
      .match(GithubReposRequest::class.java, ::fetchRepos)
      .matchAny { unknownMessage(it) }
      .build()

  override fun createReceive(): Receive {
    return behavior
  }

  private fun fetchRepos(request: GithubReposRequest) {
    val username = request.username
    val httpRequest = HttpRequest.create("https://api.github.com/users/$username/repos")
    val http = Http.get(context().system())
    val system = context().system()

    Source.single(httpRequest)
        .mapAsync(1, http::singleRequest)
        .flatMapConcat { extractEntityData(it) }
        .fold(ByteString.empty()) { acc, b -> acc.concat(b) }
        .map { deserialize(it) }
        .runWith(Sink.actorRef(sender, Status.Success(null)), system)
  }

  private fun extractEntityData(httpResponse: HttpResponse): Source<ByteString, *> {
    return if (httpResponse.status() === StatusCodes.OK) {
      httpResponse.entity().dataBytes
    } else {
      Source.failed(RuntimeException("illegal response $httpResponse"))
    }
  }

  private fun deserialize(it: ByteString): GithubReposResponse {
    val json = it.decodeString(Charsets.UTF_8)
    val response = om.readValue<List<GithubRepo>>(json)
    val reply = GithubReposResponse(response.map { r -> r.name })
    return reply
  }

  private fun unknownMessage(msg: Any) {
    logger.warn { "Unknown message $msg" }
    unhandled(msg)
  }

  companion object : KLogging()
}