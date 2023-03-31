package ru.nsu.isachenko.manager.api.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.StatusResponse
import ru.nsu.isachenko.manager.api.storage.TasksStorage

@Service
class WorkerService(
    @Autowired
    private val tasksStorage: TasksStorage,
    @Autowired
    private val workerRestTemplateService: WorkerRestTemplateService,
) {

    fun postTasks(request: CrackRequest): String {
        val id = tasksStorage.saveTask(request, 1)
        workerRestTemplateService.postTask(
            hash = request.hash,
            maxLength = request.maxLength,
            requestId = id,
        )
        return id
    }

    fun getStatus(id: String): StatusResponse {
        return tasksStorage.getJobStatus(id)
    }
}