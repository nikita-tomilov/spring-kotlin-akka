package ru.ifmo.springkotlinakka.messages

data class GithubReposRequest(
    val username: String
)

data class GithubReposResponse(
    val repos: List<String>
)