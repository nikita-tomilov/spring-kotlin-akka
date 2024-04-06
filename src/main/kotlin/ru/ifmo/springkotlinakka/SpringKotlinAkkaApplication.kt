package ru.ifmo.springkotlinakka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class SpringKotlinAkkaApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinAkkaApplication>(*args)
}
