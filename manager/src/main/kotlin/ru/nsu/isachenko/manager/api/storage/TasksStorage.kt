package ru.nsu.isachenko.manager.api.storage

import ru.nsu.isachenko.crackhash.service.model.CrackRequest
import ru.nsu.isachenko.crackhash.service.model.Status
import ru.nsu.isachenko.manager.api.storage.model.CrackHashTask
import java.util.UUID

class TasksStorage {

    private val cache = mutableMapOf<String, CrackHashTask>()

    fun saveTask(data: CrackRequest): String {
        val id = UUID.randomUUID().toString()
        cache[id] = CrackHashTask(
            data = data,
            status = Status.IN_PROGRESS
        )

        return id
    }
}