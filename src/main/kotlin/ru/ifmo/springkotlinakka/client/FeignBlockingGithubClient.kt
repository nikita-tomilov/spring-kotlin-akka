package ru.ifmo.springkotlinakka.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(value = "github", url = "https://api.github.com")
interface FeignBlockingGithubClient {

  @GetMapping(path = ["/users/{username}/repos"])
  fun fetchRepositories(@PathVariable username: String): List<Any>
}