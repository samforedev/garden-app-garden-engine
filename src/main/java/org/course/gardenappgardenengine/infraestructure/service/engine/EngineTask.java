package org.course.gardenappgardenengine.infraestructure.service.engine;

import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.infraestructure.service.entity.SensorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EngineTask {

    private final SensorService sensorService;

    @Scheduled(cron = "0 */5 * * * *")
    public void executeEngineTask() {
        System.out.println("Ejecutando tarea del motor a la hora exacta: " + java.time.LocalDateTime.now());
        sensorService.calculateReadings();
    }

}
