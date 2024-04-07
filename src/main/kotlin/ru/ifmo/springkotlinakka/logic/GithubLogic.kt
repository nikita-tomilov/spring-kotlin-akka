package ru.ifmo.springkotlinakka.logic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ifmo.springkotlinakka.client.FeignBlockingGithubClient

@Service
class GithubLogic(
  @Autowired private val feignBlockingGithubClient: FeignBlockingGithubClient
) {

  fun fetchRepositoriesCount(username: String): Int {
    val repos = feignBlockingGithubClient.fetchRepositories(username)
    return repos.size
  }
}