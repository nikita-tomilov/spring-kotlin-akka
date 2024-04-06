package ru.ifmo.springkotlinakka.logic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ifmo.springkotlinakka.client.FeignBlockingGithubClient

@Service
class GithubLogic(
    @Autowired private val feignBlockingGithubClient: FeignBlockingGithubClient
) {

  fun fetchRepositoriesSummary(username: String): String {
    val repos = feignBlockingGithubClient.fetchRepositories(username)
    return "User $username has ${repos.size} repositories"
  }
}