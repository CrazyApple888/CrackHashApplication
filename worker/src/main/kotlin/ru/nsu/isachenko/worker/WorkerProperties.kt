package ru.nsu.isachenko.worker

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "crackhash")
class WorkerProperties(
    var managerEndpoint: String = "",
)