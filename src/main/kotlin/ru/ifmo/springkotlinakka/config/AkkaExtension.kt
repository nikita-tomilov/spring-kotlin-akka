package ru.ifmo.springkotlinakka.config

import akka.actor.AbstractExtensionId
import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.Props
import org.springframework.context.ApplicationContext

class AkkaExtension : AbstractExtensionId<AkkaExtension.SpringExt?>() {
  override fun createExtension(system: ExtendedActorSystem): SpringExt {
    return SpringExt()
  }

  class SpringExt : Extension {
    private lateinit var applicationContext: ApplicationContext

    fun initialize(applicationContext: ApplicationContext) {
      this.applicationContext = applicationContext
    }

    fun props(actorBeanName: String): Props {
      return Props.create(AkkaActorProducer::class.java, applicationContext, actorBeanName)
    }
  }

  companion object {
    val EXT_PROV: AkkaExtension = AkkaExtension()
  }
}