package org.course.gardenappgardenengine.application.service;

import org.course.gardenappgardenengine.domain.models.engine.DataRecord;

public interface IPlantService {
    DataRecord activateProcess(DataRecord dataRecord) throws Exception;
    DataRecord deactivateProcess(DataRecord dataRecord) throws Exception;
    DataRecord deleteProcess(DataRecord dataRecord) throws Exception;
}
