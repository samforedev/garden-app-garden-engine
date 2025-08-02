package org.course.gardenappgardenengine.domain.repositories.entity;

import org.course.gardenappgardenengine.domain.models.entity.plant.Plant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface IPlantRepository extends MongoRepository<Plant, UUID> {
    Optional<Plant> findByIdAndDeletedFalse(UUID id);
}
