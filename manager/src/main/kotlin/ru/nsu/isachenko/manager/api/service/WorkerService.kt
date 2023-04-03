package ru.nsu.isachenko.manager.api.service

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Unmarshaller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nsu.isachenko.CrackHashWorkerResponse
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.StatusResponse
import ru.nsu.isachenko.manager.api.storage.TasksStorage
import ru.nsu.isachenko.manager.config.CrackHashConfig
import java.io.StringReader

@Service
class WorkerService(
    @Autowired
    private val tasksStorage: TasksStorage,
    @Autowired
    private val workerRestTemplateService: WorkerRestTemplateService,
) {

    fun postTasks(request: CrackRequest): String {
        val id = tasksStorage.saveTask(request, 1)
        for (taskId in 0 until CrackHashConfig.workersCount) {
            performTaskOnWorker(
                request = request,
                requestId = id,
                taskId = taskId
            )
        }
        return id
    }

    private fun performTaskOnWorker(request: CrackRequest, requestId: String, taskId: Int) {
        workerRestTemplateService.postTask(
            hash = request.hash,
            maxLength = request.maxLength,
            requestId = requestId,
            taskId = taskId
        )
    }

    fun updateTask(response: String) {
        val responseXml = runCatching {
            readXml(response!!)
        }.getOrNull()
        tasksStorage.updateTask(
            response = responseXml,
            taskId = responseXml?.partNumber ?: 0
        )
    }

    private fun readXml(xml: String): CrackHashWorkerResponse {
        val context = JAXBContext.newInstance(CrackHashWorkerResponse::class.java)
        val unmarshaller: Unmarshaller = context.createUnmarshaller()
        val reader = StringReader(xml)
        return unmarshaller.unmarshal(reader) as CrackHashWorkerResponse
    }

    fun getStatus(id: String): StatusResponse {
        return tasksStorage.getJobStatus(id)
    }
}