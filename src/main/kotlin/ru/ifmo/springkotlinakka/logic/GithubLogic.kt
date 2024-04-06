package ru.ifmo.springkotlinakka.logic

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ifmo.springkotlinakka.client.GithubClient

@Service
class GithubLogic(
    @Autowired private val githubClient: GithubClient
) {

  fun fetchRepositoriesSummary(username: String): String {
    val repos = githubClient.fetchRepositories(username)
    return "User $username has ${repos.size} repositories"
  }
}