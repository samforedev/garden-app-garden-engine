package org.course.gardenappgardenengine.domain.repositories.engine;

import org.course.gardenappgardenengine.domain.models.engine.EngineProcess;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface IEngineProcessRepository extends MongoRepository<EngineProcess, UUID> {
    Optional<EngineProcess> findByProcessId(UUID processId);
}
