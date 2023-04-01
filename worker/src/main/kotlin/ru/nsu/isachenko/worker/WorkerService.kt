package ru.nsu.isachenko.worker

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import jakarta.xml.bind.Unmarshaller
import org.apache.tomcat.util.codec.binary.Base64
import org.paukov.combinatorics.CombinatoricsFactory.createPermutationWithRepetitionGenerator
import org.paukov.combinatorics.CombinatoricsFactory.createVector
import org.paukov.combinatorics.Generator
import org.paukov.combinatorics.ICombinatoricsVector
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import org.springframework.web.client.RestTemplate
import ru.nsu.isachenko.CrackHashManagerRequest
import ru.nsu.isachenko.CrackHashWorkerResponse
import java.io.StringReader
import java.io.StringWriter


@Service
class WorkerService {

    fun compute(request: CrackHashManagerRequest): String {
        val answers = findWord(request)
        return CrackHashWorkerResponse().apply {
            setAnswers(
                CrackHashWorkerResponse.Answers().apply {
                    words.addAll(answers)
                }
            )
            partNumber = request.partNumber
            requestId = request.requestId
        }.convertToString()

//        Single.just(request)
//            .map {
//                val answers = findWord(request)
//                it to answers
//            }
//            .observeOn(Schedulers.computation())
//            .subscribeOn(Schedulers.io())
//            .subscribeBy { (request, answers) ->
//                sendWords(request, answers)
//            }
    }

    fun readXml(xml: String?): CrackHashManagerRequest {
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

//    private fun sendWords(request: CrackHashManagerRequest, answers: List<String>) {
//        val rest = RestTemplate()
//        val response = CrackHashWorkerResponse().apply {
//            setAnswers(
//                CrackHashWorkerResponse.Answers().apply {
//                    words.addAll(answers)
//                }
//            )
//            partNumber = request.partNumber
//            requestId = request.requestId
//        }.convertToHttp()
//
//        val requestFactory = HttpComponentsClientHttpRequestFactory()
//        rest.requestFactory = requestFactory
//        rest.patchForObject("http://localhost:8080/internal/api/manager/hash/crack/request", response, Any::class.java)
//    }

    private fun String.md5(): String {
        return Base64.encodeBase64String(DigestUtils.md5Digest(toByteArray()))
    }

    private fun CrackHashWorkerResponse.convertToString(): String {
        val jaxbContext: JAXBContext = JAXBContext.newInstance(CrackHashWorkerResponse::class.java)
        val marshaller: Marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        val sw = StringWriter()
        marshaller.marshal(this, sw)
        return sw.toString()
    }

//    private fun CrackHashWorkerResponse.convertToHttp(): HttpEntity<String> {
//        val jaxbContext: JAXBContext = JAXBContext.newInstance(CrackHashWorkerResponse::class.java)
//        val marshaller: Marshaller = jaxbContext.createMarshaller()
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
//        val sw = StringWriter()
//        marshaller.marshal(this, sw)
//        val xmlString: String = sw.toString()
//        val headers = HttpHeaders()
//        headers.contentType = MediaType.APPLICATION_XML
//        return HttpEntity(xmlString, headers)
//    }
}