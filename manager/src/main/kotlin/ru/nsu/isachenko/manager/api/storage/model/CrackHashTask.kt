package ru.nsu.isachenko.manager.api.storage.model

import ru.nsu.isachenko.crackhash.service.model.CrackRequest
import ru.nsu.isachenko.crackhash.service.model.Status

data class CrackHashTask(
    val data: CrackRequest,
    val status: Status
)