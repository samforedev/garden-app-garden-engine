package org.course.gardenappgardenengine.infraestructure.service.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.application.engine.IEntityAssignmentHandler;
import org.course.gardenappgardenengine.application.service.IGardenPlantService;
import org.course.gardenappgardenengine.application.service.IGardenService;
import org.course.gardenappgardenengine.domain.models.engine.*;
import org.course.gardenappgardenengine.domain.models.entity.garden.Garden;
import org.course.gardenappgardenengine.domain.repositories.engine.IEngineProcessRepository;
import org.course.gardenappgardenengine.domain.repositories.entity.IGardenRepository;
import org.course.gardenappgardenengine.shared.audit.AuditLog;
import org.course.gardenappgardenengine.shared.audit.AuditUser;
import org.course.gardenappgardenengine.shared.audit.ILogPublisher;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GardenAssignmentHandler implements IEntityAssignmentHandler {

    private final IGardenService gardenService;
    private final IGardenPlantService gardenPlantService;
    private final IGardenRepository gardenRepository;
    private final ILogPublisher logPublisher;
    private final IEngineProcessRepository engineProcessRepository;

    @Override
    public EntityType getEntityType() {
        return EntityType.GARDEN;
    }

    @Override
    public void process(ProcessType process, List<DataRecord> dataRecords, EngineProcess engineProcess) throws Exception {
        switch (process) {
            case ACTIVATE:
                List<DataRecord> activateResults = activateGardens(dataRecords);
                updateEngineProcess(engineProcess, activateResults);
                break;
            case DEACTIVATE:
                List<DataRecord> deactivateResults = deactivateGardens(dataRecords);
                updateEngineProcess(engineProcess, deactivateResults);
                break;
            case DELETE:
                List<DataRecord> deleteResults = deleteGardens(dataRecords);
                updateEngineProcess(engineProcess, deleteResults);
                break;
            case ADD:
                List<DataRecord> addPlantResults = addPlantsToGarden(engineProcess);
                updateEngineProcess(engineProcess, addPlantResults);
                break;
            case REMOVE:
                List<DataRecord> removePlantResults = removePlantsToGarden(engineProcess);
                updateEngineProcess(engineProcess, removePlantResults);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Process: " + process);
        }
    }

    @Override
    public void updateEngineProcess(EngineProcess engineProcess, List<DataRecord> dataRecords) {
        boolean isFailed = dataRecords.stream()
                .anyMatch(record -> record.getStatus() == ProcessStatus.FAILED);
        if (isFailed) {
            engineProcess.setStatus(ProcessStatus.COMPLETED_WITH_ERROR);
        } else {
            engineProcess.setStatus(ProcessStatus.COMPLETED);
        }
        engineProcess.setDataRecords(dataRecords);
        engineProcessRepository.save(engineProcess);
    }

    private List<DataRecord> activateGardens(List<DataRecord> dataRecords) throws Exception {
        List<DataRecord> results = new ArrayList<>();
        for (DataRecord dataRecord : dataRecords) {
            results.add(gardenService.activateProcess(dataRecord));
        }
        return results;
    }

    private List<DataRecord> deactivateGardens(List<DataRecord> dataRecords) throws Exception {
        List<DataRecord> results = new ArrayList<>();
        for (DataRecord dataRecord : dataRecords) {
            results.add(gardenService.deactivateProcess(dataRecord));
        }
        return results;
    }

    private List<DataRecord> deleteGardens(List<DataRecord> dataRecords) throws Exception {
        List<DataRecord> results = new ArrayList<>();
        for (DataRecord dataRecord : dataRecords) {
            results.add(gardenService.deleteProcess(dataRecord));
        }
        return results;
    }

    private List<DataRecord> addPlantsToGarden(EngineProcess engineProcess) throws Exception {
        String gardenId = engineProcess.getExtraData().get("GARDEN_ID");
        if (gardenId == null) throw new Exception("GARDEN_ID is null");

        List<DataRecord> results = new ArrayList<>();
        Optional<Garden> gardenFound = gardenRepository.findByIdAndDeletedIsFalse(UUID.fromString(gardenId));
        if (gardenFound.isEmpty()) {
            results.add(DataRecord.builder()
                    .entityId(UUID.fromString(gardenId))
                    .status(ProcessStatus.FAILED)
                    .statusReason("GARDEN: " +gardenId+ " NOT FOUND")
                    .build());
            return results;
        }

        for (DataRecord dataRecord : engineProcess.getDataRecords()) {
            results.add(gardenPlantService.addProcess(gardenFound.get().getPlantsCodes(), dataRecord));
        }

        generateLog(gardenFound.get(), results);
        return results;
    }

    private List<DataRecord> removePlantsToGarden(EngineProcess engineProcess) throws Exception {
        String gardenId = engineProcess.getExtraData().get("GARDEN_ID");
        if (gardenId == null) throw new Exception("GARDEN_ID is null");

        List<DataRecord> results = new ArrayList<>();
        Optional<Garden> gardenFound = gardenRepository.findByIdAndDeletedIsFalse(UUID.fromString(gardenId));
        if (gardenFound.isEmpty()) {
            results.add(DataRecord.builder()
                    .entityId(UUID.fromString(gardenId))
                    .status(ProcessStatus.FAILED)
                    .statusReason("GARDEN: " +gardenId+ " NOT FOUND")
                    .build());
            return results;
        }

        for (DataRecord dataRecord : engineProcess.getDataRecords()) {
            results.add(gardenPlantService.removeProcess(gardenFound.get().getPlantsCodes(), dataRecord));
        }

        generateLog(gardenFound.get(), results);
        return results;
    }

    private void generateLog(Garden garden, List<DataRecord> results) throws JsonProcessingException {
        boolean isSuccess = results.stream()
                .anyMatch(record -> record.getStatus() == ProcessStatus.DONE);

        if (isSuccess) {
            AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    garden.getId().toString(),
                    "GARDEN PLANT",
                    "ADD PLANT",
                    new AuditUser("", ""),
                    LocalDateTime.now(),
                    "",
                    new HashMap<>()
            );
            logPublisher.publish(log);
        }
    }

}
