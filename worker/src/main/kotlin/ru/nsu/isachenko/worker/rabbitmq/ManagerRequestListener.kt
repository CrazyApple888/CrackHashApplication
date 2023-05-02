package ru.nsu.isachenko.worker.rabbitmq

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nsu.isachenko.CrackHashManagerRequest
import ru.nsu.isachenko.worker.WorkerService

@Service
class ManagerRequestListener(
    @Autowired
    private val workerService: WorkerService
) {

    @RabbitListener(queues = ["q.manager-request"])
    fun onManagerRequest(managerRequest: CrackHashManagerRequest) {
        workerService.compute(managerRequest)
    }
}