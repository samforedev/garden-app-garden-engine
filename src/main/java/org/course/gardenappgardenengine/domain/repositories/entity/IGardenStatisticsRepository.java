package org.course.gardenappgardenengine.domain.repositories.entity;

import org.course.gardenappgardenengine.domain.models.entity.sensors.GardenStatistics;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface IGardenStatisticsRepository extends MongoRepository<GardenStatistics, UUID> { }
