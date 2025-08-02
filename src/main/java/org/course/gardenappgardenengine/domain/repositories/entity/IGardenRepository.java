package org.course.gardenappgardenengine.domain.repositories.entity;

import org.course.gardenappgardenengine.domain.models.entity.garden.Garden;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface IGardenRepository extends MongoRepository<Garden, UUID> {
    Optional<Garden> findByIdAndDeletedIsFalse(UUID id);
}
