package ru.nsu.isachenko.manager.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "crackhash")
class ManagerProperties(
    var workerEndpoint: String = "",
)