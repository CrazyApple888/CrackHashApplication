package ru.nsu.isachenko.manager.rabbitmq

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nsu.isachenko.CrackHashWorkerResponse
import ru.nsu.isachenko.manager.api.storage.TasksStorage

@Service
class WorkerResponseListener(
    @Autowired
    private val tasksStorage: TasksStorage
) {

    @RabbitListener(queues = ["q.worker-response"], concurrency = "10")
    fun onManagerRequest(workerResponse: CrackHashWorkerResponse) {
        tasksStorage.updateTask(workerResponse, workerResponse.partNumber)
    }
}