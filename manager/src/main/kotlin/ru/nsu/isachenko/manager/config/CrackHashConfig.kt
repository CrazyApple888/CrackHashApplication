package ru.nsu.isachenko.manager.config

object CrackHashConfig {

    val alphabet: List<String>
        get() = ('a'..'z').map(Char::toString).toList() + ('A'..'Z').map(Char::toString).toList()

    val workersCount: Int
        get() = 1

    val baseWorkerPort: String
        get() = "8080"
}