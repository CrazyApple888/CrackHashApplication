package ru.nsu.isachenko.manager.api.model

class StatusResponse(
    val status: Status,
    val data: List<String>? = null
)

enum class Status{
    WAITING,
    IN_PROGRESS,
    READY,
    ERROR;
}