package ru.nsu.isachenko.manager.api.controller

import org.apache.tomcat.util.codec.binary.Base64
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.*
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.CrackResponse
import ru.nsu.isachenko.manager.api.model.StatusResponse
import ru.nsu.isachenko.manager.api.service.WorkerService
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter


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

    @GetMapping("/generate")
    fun generateHash(@RequestParam("word") word: String): String {
        return DatatypeConverter.printHexBinary(DigestUtils.md5Digest(word.toByteArray()))
    }
}