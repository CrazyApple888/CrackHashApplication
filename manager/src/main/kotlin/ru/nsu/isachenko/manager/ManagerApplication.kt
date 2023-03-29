package ru.nsu.isachenko.manager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication


@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class ManagerApplication

fun main(args: Array<String>) {
    runApplication<ManagerApplication>(*args)
}
