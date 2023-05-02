package ru.nsu.isachenko.worker

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import jakarta.xml.bind.Unmarshaller
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator
import org.paukov.combinatorics.CombinatoricsFactory.createVector
import org.paukov.combinatorics.Generator
import org.paukov.combinatorics.ICombinatoricsVector
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import org.springframework.web.client.RestTemplate
import ru.nsu.isachenko.CrackHashManagerRequest
import ru.nsu.isachenko.CrackHashWorkerResponse
import java.io.StringReader
import java.io.StringWriter
import javax.xml.bind.DatatypeConverter


@Service
class WorkerService(
    private val workerProperties: WorkerProperties,
) {

    fun compute(request: CrackHashManagerRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val answers = findWord(request)
            val response = CrackHashWorkerResponse().apply {
                setAnswers(
                    CrackHashWorkerResponse.Answers().apply {
                        words.addAll(answers)
                    }
                )
                partNumber = request.partNumber
                requestId = request.requestId
            }.convertToString()

            sendAnswer(response)
        }
    }

    private fun sendAnswer(response: String) {
        val rest = HttpComponentsClientHttpRequestFactory().let {
            RestTemplate(it)
        }

        rest.patchForObject(
            workerProperties.managerEndpoint,
            response,
            Any::class.java
        )
    }

    fun readXml(xml: String): CrackHashManagerRequest {
        val context = JAXBContext.newInstance(CrackHashManagerRequest::class.java)
        val unmarshaller: Unmarshaller = context.createUnmarshaller()
        val reader = StringReader(xml)
        return unmarshaller.unmarshal(reader) as CrackHashManagerRequest
    }

    private fun findWord(request: CrackHashManagerRequest): List<String> {
        val vector: ICombinatoricsVector<String> = createVector(request.alphabet.symbols)
        val answers = mutableListOf<String>()
        for (len in 1..request.maxLength) {
            val gen: Generator<String> = createPermutationWithRepetitionGenerator(vector, len)
            for (item in gen) {
                val word = item.joinToString(separator = "")
                if (word.md5() == request.hash) {
                    answers.add(word)
                }
            }
        }

        return answers
    }

    private fun String.md5(): String {
        return DatatypeConverter.printHexBinary(DigestUtils.md5Digest(toByteArray()))
    }

    private fun CrackHashWorkerResponse.convertToString(): String {
        val jaxbContext: JAXBContext = JAXBContext.newInstance(CrackHashWorkerResponse::class.java)
        val marshaller: Marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        val sw = StringWriter()
        marshaller.marshal(this, sw)
        return sw.toString()
    }
}