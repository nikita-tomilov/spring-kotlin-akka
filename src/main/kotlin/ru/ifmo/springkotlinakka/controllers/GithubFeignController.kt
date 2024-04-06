package ru.ifmo.springkotlinakka.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.springkotlinakka.logic.GithubLogic

@RestController
class GithubFeignController(
  @Autowired private val githubLogic: GithubLogic
) {

  @GetMapping("/fetchFeign")
  fun fetchHelp(): String {
    return "Please use /fetchFeign/{username} to fetch repositories summary, example: /fetchFeign/avplatonov"
  }

  @GetMapping("/fetchFeign/{username}")
  fun fetchRepositoriesSummary(@PathVariable username: String): String {
    return githubLogic.fetchRepositoriesSummary(username)
  }
}