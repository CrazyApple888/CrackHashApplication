package ru.nsu.isachenko.worker

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI

@RestController
@RequestMapping("/internal/api/worker/hash/crack")
class WorkerController {

    @PostMapping("/task")
    fun postTask() {
        RestTemplate().apply {
            //todo
            val uri = URI.create("/internal/api/worker/hash/crack/task")
            postForEntity(uri, null, Any::class.java)
        }
    }
}