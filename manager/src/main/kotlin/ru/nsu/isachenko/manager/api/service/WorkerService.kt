package ru.nsu.isachenko.manager.api.service

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Unmarshaller
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nsu.isachenko.CrackHashWorkerResponse
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.StatusResponse
import ru.nsu.isachenko.manager.api.storage.TasksStorage
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

    fun updateTask(taskId: Int, response: String) {
        val responseXml = readXml(response)
        tasksStorage.updateTask(
            response = responseXml,
            taskId = taskId
        )
    }

    fun readXml(xml: String): CrackHashWorkerResponse {
        val context = JAXBContext.newInstance(CrackHashWorkerResponse::class.java)
        val unmarshaller: Unmarshaller = context.createUnmarshaller()
        val reader = StringReader(xml)
        return unmarshaller.unmarshal(reader) as CrackHashWorkerResponse
    }
}