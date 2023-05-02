package ru.nsu.isachenko.manager.api.storage.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import ru.nsu.isachenko.manager.api.model.CrackRequest
import ru.nsu.isachenko.manager.api.model.Status

@Document("crackJob")
class CrackHashJob {

    @Id var id: String = ""
    var data: CrackRequest = CrackRequest()
    var tasks: MutableMap<Int, CrackHashTask> = mutableMapOf()


    class CrackHashTask {
        @Id
        var id: String = ""
        var status: Status = Status.IN_PROGRESS
        var answers: List<String>? = null
    }
}