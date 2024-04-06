package ru.ifmo.springkotlinakka.config

import akka.actor.ActorSystem
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import ru.ifmo.springkotlinakka.config.AkkaExtension.Companion.EXT_PROV

@Configuration
@ComponentScan
class AkkaConfig {

  @Bean
  @ConditionalOnMissingBean(ActorSystem::class)
  fun getActorSystem(applicationContext: ApplicationContext): ActorSystem {
    val system = ActorSystem.create("spring-kotlin-akka")
    EXT_PROV.get(system)!!.initialize(applicationContext)
    return system
  }
}