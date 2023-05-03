package ru.nsu.isachenko.worker

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.paukov.combinatorics3.Generator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import org.springframework.web.client.RestTemplate
import ru.nsu.isachenko.CrackHashManagerRequest
import ru.nsu.isachenko.CrackHashWorkerResponse
import ru.nsu.isachenko.worker.rabbitmq.WorkerResponsePublisher
import javax.xml.bind.DatatypeConverter
import kotlin.math.ceil
import kotlin.math.pow


@Service
class WorkerService(
    @Autowired
    private val workerResponsePublisher: WorkerResponsePublisher
) {

    fun compute(request: CrackHashManagerRequest) {
        val answers = findWord(request)
        println("found solution")
        val response = CrackHashWorkerResponse().apply {
            setAnswers(
                CrackHashWorkerResponse.Answers().apply {
                    words.addAll(answers)
                }
            )
            partNumber = request.partNumber
            requestId = request.requestId
        }

        println("send answer")
        workerResponsePublisher.publishResponse(response)
    }

    fun findWord(request: CrackHashManagerRequest): List<String> {
        val words: MutableList<String> = ArrayList()
        for (length in 1..request.maxLength) {
            val allWordCount = request.alphabet.symbols.size.toDouble().pow(length.toDouble()).toInt()
            val start = start(request.partNumber.toLong(), request.partCount.toLong(), allWordCount.toLong())
            val partWordCount =
                currPartCount(request.partNumber.toLong(), request.partCount.toLong(), allWordCount.toLong())
            words.addAll(
                Generator.permutation(request.alphabet.symbols)
                    .withRepetitions(length)
                    .stream()
                    .skip(start)
                    .limit(partWordCount)
                    .map { word -> word.joinToString(separator = "") }
                    .filter { word ->
                        request.hash == word.md5()
                    }
                    .toList())
        }
        return words
    }

    private fun start(partNumber: Long, partCount: Long, words: Long): Long =
        ceil(words.toDouble() / partCount * partNumber).toLong()

    private fun currPartCount(partNumber: Long, partCount: Long, words: Long): Long {
        return (ceil(words.toDouble() / partCount * (partNumber + 1)) - ceil(words.toDouble() / partCount * partNumber)).toLong()
    }

    private fun String.md5(): String {
        return DatatypeConverter.printHexBinary(DigestUtils.md5Digest(toByteArray()))
    }
}