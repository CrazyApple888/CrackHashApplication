package ru.nsu.isachenko.manager.api.storage.model

import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.Status

data class CrackHashJob(
    val data: CrackRequest,
    val tasks: MutableMap<Int, CrackHashTask>
) {

    class CrackHashTask(
        val status: Status = Status.IN_PROGRESS,
        val answers: List<String>? = null
    )
}