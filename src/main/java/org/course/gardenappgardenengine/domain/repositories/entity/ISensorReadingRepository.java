package org.course.gardenappgardenengine.domain.repositories.entity;

import org.course.gardenappgardenengine.domain.models.entity.sensors.Readings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ISensorReadingRepository extends MongoRepository<Readings, UUID> {
    List<Readings> findAllByGardenCode(String gardenCode);
}
