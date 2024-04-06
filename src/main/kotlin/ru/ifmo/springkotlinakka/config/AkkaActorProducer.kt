package ru.ifmo.springkotlinakka.config

import akka.actor.Actor
import akka.actor.IndirectActorProducer
import org.springframework.context.ApplicationContext

class AkkaActorProducer(
  private val applicationContext: ApplicationContext,
  private val beanActorName: String
) : IndirectActorProducer {

  override fun produce(): Actor {
    return applicationContext.getBean(beanActorName) as Actor
  }

  override fun actorClass(): Class<out Actor> {
    return applicationContext.getType(beanActorName) as Class<out Actor>
  }
}