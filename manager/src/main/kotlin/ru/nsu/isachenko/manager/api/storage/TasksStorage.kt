package ru.nsu.isachenko.manager.api.storage

import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.nsu.isachenko.CrackHashWorkerResponse
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.Status
import ru.nsu.isachenko.manager.api.model.StatusResponse
import ru.nsu.isachenko.manager.api.storage.model.CrackHashJob
import java.util.*

@Component
class TasksStorage {

    private val cache = mutableMapOf<String, CrackHashJob>()

    fun saveTask(data: CrackRequest, tasksCount: Int): String {
        val id = UUID.randomUUID().toString()
        cache[id] = CrackHashJob(
            data = data,
            tasks = (0..tasksCount).associateWith { CrackHashJob.CrackHashTask() }.toMutableMap()
        )

        return id
    }

    fun updateTask(id: String, response: CrackHashWorkerResponse?, taskId: Int) {
        val job = cache[id] ?: return
        job.tasks.computeIfPresent(taskId) { _, _ ->
            CrackHashJob.CrackHashTask(
                status = if (response == null) Status.ERROR else Status.READY,
                answers = response?.answers?.words
            )
        }
    }

    fun getJobStatus(id: String): StatusResponse {
        val job = cache[id] ?: return StatusResponse(status = Status.ERROR)
        val hasErrors = job.tasks.values.find { it.status == Status.ERROR } != null
        val hasProgress = job.tasks.values.find { it.status == Status.IN_PROGRESS } != null

        return when {
            hasErrors -> StatusResponse(status = Status.ERROR)
            hasProgress -> StatusResponse(status = Status.IN_PROGRESS)
            else -> StatusResponse(
                status = Status.READY,
                data = job.tasks.values.flatMap { it.answers ?: emptyList() }.distinct()
            )
        }
    }
}