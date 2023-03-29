package ru.nsu.isachenko.crackhash.service.controller

import org.springframework.web.bind.annotation.*
import ru.nsu.isachenko.crackhash.service.model.CrackRequest
import ru.nsu.isachenko.crackhash.service.model.CrackResponse
import ru.nsu.isachenko.crackhash.service.model.Status
import ru.nsu.isachenko.crackhash.service.model.StatusResponse


@RestController
@RequestMapping("/api/hash")
class CrackController {

    @PostMapping("/crack")
    fun crackHash(@RequestBody body: CrackRequest): CrackResponse {
        return CrackResponse(
            id = "${body.hash} ${body.maxLength}"
        )
    }

    @GetMapping("/status")
    fun status(@RequestParam("requestId") requestId: String): StatusResponse {
        return StatusResponse(
            status = Status.IN_PROGRESS
        )
    }
}