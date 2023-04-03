package ru.nsu.isachenko.manager.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.nsu.isachenko.manager.api.service.WorkerService

@RestController
@RequestMapping("/internal/api/manager/hash/crack")
class InternalManagerController(
    @Autowired
    private val workerService: WorkerService
) {

    @PatchMapping("/request")
    fun request(@RequestBody body: String) {
        workerService.updateTask(body)
    }
}