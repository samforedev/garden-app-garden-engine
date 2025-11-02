package org.course.gardenappgardenengine.domain.models.entity.sensors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.course.gardenappgardenengine.domain.models.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "gardenStatistics")
public class GardenStatistics extends BaseEntity {
    private String gardenCode;
    private Date summaryDate;
    private List<SensorStatistic> statistics;
}
