package ru.nsu.isachenko.manager.api.storage

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.nsu.isachenko.CrackHashWorkerResponse
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.Status
import ru.nsu.isachenko.manager.api.model.StatusResponse
import ru.nsu.isachenko.manager.api.storage.model.CrackHashJob
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Component
class TasksStorage(
    private val database: CrackHashRepository,
) {

    fun saveTask(data: CrackRequest, tasksCount: Int): String {
        val job = CrackHashJob().apply {
            id = UUID.randomUUID().toString()
            this.data = data
            tasks = (0 until tasksCount).associateWith { CrackHashJob.CrackHashTask() }.toMutableMap()
        }

        return database.save(job).id
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun updateTask(response: CrackHashWorkerResponse?, taskId: Int) {
        println(response?.requestId)
        val job = response?.requestId?.let { database.findById(it) }?.get() ?: return
        job.tasks.computeIfPresent(taskId) { _, _ ->
            CrackHashJob.CrackHashTask().apply {
                status = Status.READY
                answers = response.answers?.words
            }
        }
        if (job.tasks.all { it.value.status == Status.READY }) {
            job.status = Status.READY
        }
        database.save(job)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getJobStatus(id: String): StatusResponse {
        val job = database.findById(id).getOrNull() ?: return StatusResponse(status = Status.ERROR)
        val hasErrors = job.tasks.values.find { it.status == Status.ERROR } != null
        val hasProgress = job.tasks.values.find { it.status == Status.IN_PROGRESS } != null
        val hasWaiting = job.tasks.values.find { it.status == Status.WAITING } != null
        val status = when {
            hasErrors -> Status.ERROR
            hasProgress -> Status.IN_PROGRESS
            hasWaiting -> Status.WAITING
            else -> Status.READY
        }

        return StatusResponse(
            data = job.tasks.values.flatMap { it.answers ?: emptyList() }.distinct(),
            status = status
        )
    }
}