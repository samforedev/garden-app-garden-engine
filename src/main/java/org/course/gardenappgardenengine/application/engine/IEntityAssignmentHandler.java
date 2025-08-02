package org.course.gardenappgardenengine.application.engine;

import org.course.gardenappgardenengine.domain.models.engine.DataRecord;
import org.course.gardenappgardenengine.domain.models.engine.EngineProcess;
import org.course.gardenappgardenengine.domain.models.engine.EntityType;
import org.course.gardenappgardenengine.domain.models.engine.ProcessType;

import java.util.List;

public interface IEntityAssignmentHandler {
    EntityType getEntityType();
    void process(ProcessType process, List<DataRecord> dataRecords, EngineProcess engineProcess) throws Exception;
    void updateEngineProcess(EngineProcess engineProcess, List<DataRecord> dataRecords) throws Exception;
}
