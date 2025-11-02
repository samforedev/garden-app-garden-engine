package org.course.gardenappgardenengine.domain.models.entity.sensors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.course.gardenappgardenengine.domain.models.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorStatistic {
    private String type;
    private double avg;
    private double min;
    private double max;
    private int count;
}
