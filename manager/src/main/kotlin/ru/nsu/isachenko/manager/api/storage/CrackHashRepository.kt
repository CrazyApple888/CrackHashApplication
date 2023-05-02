package ru.nsu.isachenko.manager.api.storage

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import ru.nsu.isachenko.manager.api.storage.model.CrackHashJob

@Repository("crackHashRepository")
interface CrackHashRepository: MongoRepository<CrackHashJob, String> {
}