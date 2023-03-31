package ru.nsu.isachenko.manager.config

object CrackHashConfig {

    val alphabet: List<String>
        get() = listOf("a")

    val workersCount: Int
        get() = 1

    val baseWorkerPort: String
        get() = "8080"
}