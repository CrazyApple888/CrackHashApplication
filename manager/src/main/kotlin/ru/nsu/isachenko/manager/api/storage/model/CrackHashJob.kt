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
    var status: Status = Status.WAITING

    class CrackHashTask {
        @Id
        var id: String = ""
        var status: Status = Status.WAITING
        var answers: List<String>? = null
    }
}