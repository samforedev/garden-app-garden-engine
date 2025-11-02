package org.course.gardenappgardenengine.domain.models.entity.sensors;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.course.gardenappgardenengine.domain.models.entity.BaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "readings")
public class Readings extends BaseEntity {
    private String gardenId;
    private List<SensorData> sensors;
    private Instant timestamp;
}
