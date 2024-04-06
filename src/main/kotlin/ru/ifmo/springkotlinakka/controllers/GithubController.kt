package ru.ifmo.springkotlinakka.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import ru.ifmo.springkotlinakka.logic.GithubLogic

@RestController
class GithubController(
    @Autowired private val githubLogic: GithubLogic
) {

  @GetMapping("/fetch")
  fun fetchHelp(): String {
    return "Please use /fetch/{username} to fetch repositories summary, example: /fetch/avplatonov"
  }

  @GetMapping("/fetch/{username}")
  fun fetchRepositoriesSummary(@PathVariable username: String): String {
    return githubLogic.fetchRepositoriesSummary(username)
  }
}