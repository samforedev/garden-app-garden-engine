package org.course.gardenappgardenengine.infraestructure.service.entity;

import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.application.service.IGardenService;
import org.course.gardenappgardenengine.domain.models.engine.DataRecord;
import org.course.gardenappgardenengine.domain.models.engine.ProcessStatus;
import org.course.gardenappgardenengine.domain.models.entity.StatusEntity;
import org.course.gardenappgardenengine.domain.models.entity.garden.Garden;
import org.course.gardenappgardenengine.domain.repositories.entity.IGardenRepository;
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
public class GardenService implements IGardenService {

    private final IGardenRepository gardenRepository;
    private final ILogPublisher logPublisher;

    @Override
    public DataRecord activateProcess(DataRecord dataRecord) {
        try {
            Optional<Garden> gardenFound = gardenRepository.findByIdAndDeletedIsFalse(dataRecord.getEntityId());
            if (gardenFound.isEmpty()) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("GARDEN NOT FOUND")
                        .build();
            }

            if (gardenFound.get().getStatus() == StatusEntity.ACTIVE) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("GARDEN ALREADY ACTIVE")
                        .build();
            }

            gardenFound.get().setStatus(StatusEntity.ACTIVE);
            updateData(gardenFound.get());

            AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    gardenFound.get().getId().toString(),
                    "GARDEN",
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
            Optional<Garden> gardenFound = gardenRepository.findByIdAndDeletedIsFalse(dataRecord.getEntityId());
            if (gardenFound.isEmpty()) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("GARDEN NOT FOUND")
                        .build();
            }

            if (gardenFound.get().getStatus() == StatusEntity.INACTIVE) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("GARDEN ALREADY DEACTIVATED")
                        .build();
            }

            gardenFound.get().setStatus(StatusEntity.INACTIVE);
            updateData(gardenFound.get());

            AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    gardenFound.get().getId().toString(),
                    "GARDEN",
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
    public DataRecord deleteProcess(DataRecord dataRecord) {
        try {
            Optional<Garden> gardenFound = gardenRepository.findByIdAndDeletedIsFalse(dataRecord.getEntityId());
            if (gardenFound.isEmpty()) {
                return DataRecord.builder()
                        .entityId(dataRecord.getEntityId())
                        .status(ProcessStatus.FAILED)
                        .statusReason("GARDEN NOT FOUND")
                        .build();
            }

            gardenFound.get().setStatus(StatusEntity.DELETED);
            updateData(gardenFound.get());

            AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    gardenFound.get().getId().toString(),
                    "GARDEN",
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

    private void updateData(Garden garden) {
        garden.setUpdatedAt(LocalDateTime.now());
        garden.setVersion(UUID.randomUUID());
        gardenRepository.save(garden);
    }

}
