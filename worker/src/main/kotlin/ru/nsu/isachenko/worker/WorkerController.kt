package ru.nsu.isachenko.worker

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/internal/api/worker/hash/crack")
class WorkerController(
    @Autowired
    private val workerService: WorkerService
) {

    @PostMapping("/task")
    fun postTask(@RequestBody body: String) {
//        workerService.compute(
//            workerService.readXml(body)
//        )
    }
}