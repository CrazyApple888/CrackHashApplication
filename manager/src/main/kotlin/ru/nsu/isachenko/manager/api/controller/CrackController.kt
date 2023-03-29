package ru.nsu.isachenko.manager.api.controller

import org.springframework.web.bind.annotation.*
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.CrackResponse
import ru.nsu.isachenko.manager.api.model.StatusResponse
import ru.nsu.isachenko.manager.api.service.WorkerService


@RestController
@RequestMapping("/api/hash")
class CrackController(
    private val workerService: WorkerService
) {

    @PostMapping("/crack")
    fun crackHash(@RequestBody body: CrackRequest): CrackResponse {
        return CrackResponse(
            id = workerService.postTasks(body)
        )
    }

    @GetMapping("/status")
    fun status(@RequestParam("requestId") requestId: String): StatusResponse {
        return workerService.getStatus(requestId)
    }
}