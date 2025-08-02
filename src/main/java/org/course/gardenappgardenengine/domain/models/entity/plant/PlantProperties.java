package org.course.gardenappgardenengine.domain.models.entity.plant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlantProperties {
    private double humidity;
    private double temperature;
    private double ph;
    private Map<String, String> extraInfo;
}
