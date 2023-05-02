package ru.nsu.isachenko.worker.rabbitmq

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.MarshallingMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import ru.nsu.isachenko.CrackHashManagerRequest
import ru.nsu.isachenko.CrackHashWorkerResponse

@Configuration
class RabbitConfig {
    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange(EXCHANGE_NAME)
    }

    @Bean
    fun managerRequestQueue(): Queue {
        return Queue(MANAGER_REQUEST_QUEUE_NAME)
    }

    @Bean
    fun managerRequestBinding(exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(managerRequestQueue()).to(exchange).with(MANAGER_REQUEST_ROUTING_KEY)
    }

    @Bean
    fun workerResponseQueue(): Queue {
        return Queue(WORKER_RESPONSE_QUEUE_NAME)
    }

    @Bean
    fun workerResponseBinding(exchange: DirectExchange?): Binding {
        return BindingBuilder.bind(workerResponseQueue()).to(exchange).with(WORKER_RESPONSE_ROUTING_KEY)
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = xmlMessageConverter()
        return rabbitTemplate
    }

    @Bean
    fun xmlMessageConverter(): MessageConverter {
        val jaxb2Marshaller = Jaxb2Marshaller()
        jaxb2Marshaller.setClassesToBeBound(CrackHashManagerRequest::class.java, CrackHashWorkerResponse::class.java)
        return MarshallingMessageConverter(jaxb2Marshaller)
    }

    companion object {

        const val EXCHANGE_NAME = "e.crack-hash"
        const val MANAGER_REQUEST_ROUTING_KEY = "manager-request"
        private const val MANAGER_REQUEST_QUEUE_NAME = "q.$MANAGER_REQUEST_ROUTING_KEY"
        const val WORKER_RESPONSE_ROUTING_KEY = "worker-response"
        private const val WORKER_RESPONSE_QUEUE_NAME = "q.$WORKER_RESPONSE_ROUTING_KEY"
    }
}