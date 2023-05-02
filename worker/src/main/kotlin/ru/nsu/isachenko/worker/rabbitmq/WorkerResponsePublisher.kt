package ru.nsu.isachenko.worker.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nsu.isachenko.CrackHashWorkerResponse

@Service
class WorkerResponsePublisher(
    @Autowired
    private val rabbitTemplate: RabbitTemplate
) {

    fun publishResponse(response: CrackHashWorkerResponse) {
        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE_NAME, RabbitConfig.WORKER_RESPONSE_ROUTING_KEY, response
        )
    }
}