package ru.ifmo.springkotlinakka.messages

data class GithubReposCountRequest(
    val username: String
)

data class GithubReposCountResponse(
    val count: Int
)

data class GithubReposRequest(
    val username: String
)

data class GithubReposResponse(
    val repos: List<String>
)