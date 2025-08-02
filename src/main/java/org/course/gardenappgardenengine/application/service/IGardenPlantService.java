package org.course.gardenappgardenengine.application.service;

import org.course.gardenappgardenengine.domain.models.engine.DataRecord;

import java.util.List;

public interface IGardenPlantService {
    DataRecord addProcess(List<String> plantCodes, DataRecord dataRecord) throws Exception;
    DataRecord removeProcess(List<String> plantCodes, DataRecord dataRecord) throws Exception;
}
