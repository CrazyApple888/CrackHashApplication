package ru.nsu.isachenko.manager.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.nsu.isachenko.CrackHashManagerRequest

@Service
class ManagerRequestPublisher(
    @Autowired
    private val rabbitTemplate: RabbitTemplate
) {

    fun publishRequest(request: CrackHashManagerRequest) {
        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE_NAME, RabbitConfig.MANAGER_REQUEST_ROUTING_KEY, request
        )
    }
}