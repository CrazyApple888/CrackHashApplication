package ru.nsu.isachenko.manager.api.service

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.nsu.isachenko.CrackHashManagerRequest
import ru.nsu.isachenko.manager.config.CrackHashConfig
import ru.nsu.isachenko.manager.config.ManagerProperties
import java.io.StringWriter


@Service
class WorkerRestTemplateService(
    private val managerProperties: ManagerProperties,
) {

    fun postTask(hash: String, maxLength: Int, requestId: String, taskId: Int) {
        val restTemplate = RestTemplate()
        val request = CrackHashManagerRequest().apply {
            setRequestId(requestId)
            setHash(hash)
            setMaxLength(maxLength)
            alphabet = CrackHashManagerRequest.Alphabet().apply {
                symbols.addAll(CrackHashConfig.alphabet)
            }
            partCount = CrackHashConfig.workersCount
            partNumber = taskId
        }.convertToHttp()

        restTemplate.postForEntity(
            managerProperties.workerEndpoint,
            request,
            String::class.java
        )
    }

    private fun CrackHashManagerRequest.convertToHttp(): HttpEntity<String> {
        val jaxbContext: JAXBContext = JAXBContext.newInstance(CrackHashManagerRequest::class.java)
        val marshaller: Marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        val sw = StringWriter()
        marshaller.marshal(this, sw)
        val xmlString: String = sw.toString()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_XML
        return HttpEntity(xmlString, headers)
    }
}