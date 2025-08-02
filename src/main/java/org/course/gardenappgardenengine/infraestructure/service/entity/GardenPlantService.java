package org.course.gardenappgardenengine.infraestructure.service.entity;

import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.application.service.IGardenPlantService;
import org.course.gardenappgardenengine.domain.models.engine.DataRecord;
import org.course.gardenappgardenengine.domain.models.engine.ProcessStatus;
import org.course.gardenappgardenengine.domain.models.entity.plant.Plant;
import org.course.gardenappgardenengine.domain.repositories.entity.IPlantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GardenPlantService implements IGardenPlantService {

    private final IPlantRepository plantRepository;

    @Override
    public DataRecord addProcess(List<String> plantCodes, DataRecord dataRecord) {
        try {
            Optional<Plant> plantFound = plantRepository.findByIdAndDeletedFalse(dataRecord.getEntityId());
            if (plantFound.isEmpty())
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT NOT FOUND")
                        .build();

            boolean isPresentPlant = plantCodes.stream()
                    .anyMatch(plantCode -> plantCode.equals(plantFound.get().getCode()));

            if (isPresentPlant)
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT ALREADY BELONGS TO GARDEN")
                        .build();

            return DataRecord.builder()
                    .entityId(dataRecord.getEntityId())
                    .status(ProcessStatus.DONE)
                    .statusReason("")
                    .build();

        } catch (Exception ex) {
            return DataRecord.builder()
                    .entityId(dataRecord.getEntityId())
                    .status(ProcessStatus.FAILED)
                    .statusReason(ex.getMessage())
                    .build();
        }
    }

    @Override
    public DataRecord removeProcess(List<String> plantCodes, DataRecord dataRecord) {
        try {
            Optional<Plant> plantFound = plantRepository.findByIdAndDeletedFalse(dataRecord.getEntityId());
            if (plantFound.isEmpty())
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT NOT FOUND")
                        .build();

            boolean isPresentPlant = plantCodes.stream()
                    .anyMatch(plantCode -> plantCode.equals(plantFound.get().getCode()));

            if (!isPresentPlant)
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT DOES NOT BELONG TO GARDEN")
                        .build();

            return DataRecord.builder()
                    .entityId(dataRecord.getEntityId())
                    .status(ProcessStatus.DONE)
                    .statusReason("")
                    .build();

        } catch (Exception ex) {
            return DataRecord.builder()
                    .entityId(dataRecord.getEntityId())
                    .status(ProcessStatus.FAILED)
                    .statusReason(ex.getMessage())
                    .build();
        }
    }
}
