package org.course.gardenappgardenengine.domain.models.engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataRecord {
    @Field(targetType = FieldType.STRING)
    private UUID entityId;
    private ProcessStatus status;
    private String statusReason;
}
