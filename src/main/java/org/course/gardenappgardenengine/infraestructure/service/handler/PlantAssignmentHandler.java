package org.course.gardenappgardenengine.infraestructure.service.handler;

import lombok.RequiredArgsConstructor;
import org.course.gardenappgardenengine.application.engine.IEntityAssignmentHandler;
import org.course.gardenappgardenengine.application.service.IPlantService;
import org.course.gardenappgardenengine.domain.models.engine.*;
import org.course.gardenappgardenengine.domain.repositories.engine.IEngineProcessRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PlantAssignmentHandler implements IEntityAssignmentHandler {

    private final IPlantService plantService;
    private final IEngineProcessRepository engineProcessRepository;

    @Override
    public EntityType getEntityType() {
        return EntityType.PLANT;
    }

    @Override
    public void process(ProcessType process, List<DataRecord> dataRecords, EngineProcess engineProcess) throws Exception {
        switch (process) {
            case ACTIVATE:
                List<DataRecord> activateResults = activatePlants(dataRecords);
                updateEngineProcess(engineProcess, activateResults);
                break;
            case DEACTIVATE:
                List<DataRecord> deactivateResults = deactivatePlants(dataRecords);
                updateEngineProcess(engineProcess, deactivateResults);
                break;
            case DELETE:
                List<DataRecord> deleteResults = deletePlants(dataRecords);
                updateEngineProcess(engineProcess, deleteResults);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Process: " + process);
        }
    }

    @Override
    public void updateEngineProcess(EngineProcess engineProcess, List<DataRecord> dataRecords) {
        boolean isFailed = dataRecords.stream()
                .anyMatch(record -> record.getStatus() == ProcessStatus.FAILED);;
        if (isFailed) {
            engineProcess.setStatus(ProcessStatus.COMPLETED_WITH_ERROR);
        } else {
            engineProcess.setStatus(ProcessStatus.COMPLETED);
        }
        engineProcess.setDataRecords(dataRecords);
        engineProcessRepository.save(engineProcess);
    }

    private List<DataRecord> activatePlants(List<DataRecord> dataRecords) throws Exception {
        List<DataRecord> results = new ArrayList<>();
        for (DataRecord dataRecord : dataRecords) {
            results.add(plantService.activateProcess(dataRecord));
        }
        return results;
    }

    private List<DataRecord> deactivatePlants(List<DataRecord> dataRecords) throws Exception {
        List<DataRecord> results = new ArrayList<>();
        for (DataRecord dataRecord : dataRecords) {
            results.add(plantService.deactivateProcess(dataRecord));
        }
        return results;
    }

    private List<DataRecord> deletePlants(List<DataRecord> dataRecords) throws Exception {
        List<DataRecord> results = new ArrayList<>();
        for (DataRecord dataRecord : dataRecords) {
            results.add(plantService.deleteProcess(dataRecord));
        }
        return results;
    }

}
