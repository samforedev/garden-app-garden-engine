package org.course.gardenappgardenengine.infraestructure.service.entity;

import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.application.service.IPlantService;
import org.course.gardenappgardenengine.domain.models.engine.DataRecord;
import org.course.gardenappgardenengine.domain.models.engine.ProcessStatus;
import org.course.gardenappgardenengine.domain.models.entity.StatusEntity;
import org.course.gardenappgardenengine.domain.models.entity.plant.Plant;
import org.course.gardenappgardenengine.domain.repositories.entity.IPlantRepository;
import org.course.gardenappgardenengine.shared.audit.AuditLog;
import org.course.gardenappgardenengine.shared.audit.AuditUser;
import org.course.gardenappgardenengine.shared.audit.ILogPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlantService implements IPlantService {

    private final IPlantRepository plantRepository;
    private final ILogPublisher logPublisher;

    @Override
    public DataRecord activateProcess(DataRecord dataRecord) {
        try {
            Optional<Plant> plantFound = plantRepository.findByIdAndDeletedFalse(dataRecord.getEntityId());
            if (plantFound.isEmpty()) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT NOT FOUND")
                        .build();
            }

            if (plantFound.get().getStatus() == StatusEntity.ACTIVE) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT ALREADY ACTIVE")
                        .build();
            }

            plantFound.get().setStatus(StatusEntity.ACTIVE);
            updateData(plantFound.get());

            AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    plantFound.get().getId().toString(),
                    "PLANT",
                    "ACTIVATE MANY",
                    new AuditUser("", ""),
                    LocalDateTime.now(),
                    "",
                    new HashMap<>()
            );
            logPublisher.publish(log);

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
    public DataRecord deactivateProcess(DataRecord dataRecord) {
        try {
            Optional<Plant> plantFound = plantRepository.findByIdAndDeletedFalse(dataRecord.getEntityId());
            if (plantFound.isEmpty()) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT NOT FOUND")
                        .build();
            }

            if (plantFound.get().getStatus() == StatusEntity.INACTIVE) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT ALREADY DEACTIVATED")
                        .build();
            }

            plantFound.get().setStatus(StatusEntity.INACTIVE);
            updateData(plantFound.get());

            AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    plantFound.get().getId().toString(),
                    "PLANT",
                    "DEACTIVATE MANY",
                    new AuditUser("", ""),
                    LocalDateTime.now(),
                    "",
                    new HashMap<>()
            );
            logPublisher.publish(log);

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
    public DataRecord deleteProcess(DataRecord dataRecord) throws Exception {
        try {
            Optional<Plant> plantFound = plantRepository.findByIdAndDeletedFalse(dataRecord.getEntityId());
            if (plantFound.isEmpty()) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("PLANT NOT FOUND")
                        .build();
            }

            plantFound.get().setStatus(StatusEntity.DELETED);
            updateData(plantFound.get());

            AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    plantFound.get().getId().toString(),
                    "PLANT",
                    "DELETE MANY",
                    new AuditUser("", ""),
                    LocalDateTime.now(),
                    "",
                    new HashMap<>()
            );
            logPublisher.publish(log);

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

    private void updateData(Plant plant) {
        plant.setUpdatedAt(LocalDateTime.now());
        plant.setVersion(UUID.randomUUID());
        plantRepository.save(plant);
    }

}
