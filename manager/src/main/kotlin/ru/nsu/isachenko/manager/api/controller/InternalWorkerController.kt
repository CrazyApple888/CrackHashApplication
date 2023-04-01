package ru.nsu.isachenko.manager.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.nsu.isachenko.manager.api.service.WorkerService

@RestController
class InternalWorkerController(
    @Autowired
    private val workerService: WorkerService
) {

    @PatchMapping("/internal/api/manager/hash/crack/request")
    fun patchAnswers(@RequestBody body: String) {
        workerService.updateTask(0, body)
    }
}