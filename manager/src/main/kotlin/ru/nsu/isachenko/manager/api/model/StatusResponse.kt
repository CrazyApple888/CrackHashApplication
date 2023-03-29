package ru.nsu.isachenko.crackhash.service.model

class StatusResponse(
    val status: Status,
    val data: List<String>? = null
)

enum class Status{
    IN_PROGRESS,
    READY,
    ERROR;
}