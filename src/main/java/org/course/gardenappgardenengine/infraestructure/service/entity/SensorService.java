package org.course.gardenappgardenengine.infraestructure.service.entity;

import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.domain.models.entity.garden.Garden;
import org.course.gardenappgardenengine.domain.models.entity.sensors.GardenStatistics;
import org.course.gardenappgardenengine.domain.models.entity.sensors.Readings;
import org.course.gardenappgardenengine.domain.models.entity.sensors.SensorData;
import org.course.gardenappgardenengine.domain.models.entity.sensors.SensorStatistic;
import org.course.gardenappgardenengine.domain.repositories.entity.IGardenRepository;
import org.course.gardenappgardenengine.domain.repositories.entity.IGardenStatisticsRepository;
import org.course.gardenappgardenengine.domain.repositories.entity.ISensorReadingRepository;
import org.course.gardenappgardenengine.shared.audit.ILogPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final IGardenRepository gardenRepository;
    private final ISensorReadingRepository sensorReadingRepository;
    private final IGardenStatisticsRepository gardenStatisticsRepository;
    private final ILogPublisher logPublisher;

    public void calculateReadings() {
        List<Garden> gardens = gardenRepository.findAllByDeletedIsFalse();
        List<String> gardenCodes =  gardens.stream().map(Garden::getCode).toList();
        if (gardenCodes.isEmpty()) return;

        for (String gardenCode : gardenCodes) {
            calculateReadingsByGardenCode(gardenCode);
        }
    }

    private void calculateReadingsByGardenCode(String gardenCode) {
        List<Readings> readings = sensorReadingRepository.findAllByGardenCode(gardenCode);
        if (readings.isEmpty()) return;

        Map<String, List<Double>> groupedValues = readings.stream()
                .flatMap(r -> r.getSensors().stream())
                .collect(Collectors.groupingBy(
                        SensorData::getType,
                        Collectors.mapping(SensorData::getValue, Collectors.toList())
                ));

        List<SensorStatistic> statsList = groupedValues.entrySet().stream()
                .map(entry -> {
                    List<Double> values = entry.getValue();
                    double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                    double min = values.stream().mapToDouble(Double::doubleValue).min().orElse(0);
                    double max = values.stream().mapToDouble(Double::doubleValue).max().orElse(0);
                    return new SensorStatistic(entry.getKey(), avg, min, max, values.size());
                })
                .toList();

        GardenStatistics stats = new GardenStatistics();
        stats.setId(UUID.randomUUID());
        stats.setCreatedAt(LocalDateTime.now());
        stats.setUpdatedAt(LocalDateTime.now());
        stats.setVersion(UUID.randomUUID());
        stats.setStatistics(statsList);

        stats.setGardenCode(gardenCode);
        gardenStatisticsRepository.save(stats);

        sensorReadingRepository.deleteAll(readings);
    }

}
