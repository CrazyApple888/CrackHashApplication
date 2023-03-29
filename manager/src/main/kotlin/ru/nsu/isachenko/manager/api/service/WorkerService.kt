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
) {

    fun postTasks(request: CrackRequest): String {
        //todo post tasks to worker
        return tasksStorage.saveTask(request, 1)
    }

    fun getStatus(id: String): StatusResponse {
        return tasksStorage.getJobStatus(id)
    }
}