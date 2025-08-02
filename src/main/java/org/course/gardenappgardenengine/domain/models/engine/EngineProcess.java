package org.course.gardenappgardenengine.domain.models.engine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "engineProcess")
public class EngineProcess {
    @Id
    @Field(targetType = FieldType.STRING)
    private UUID id;
    @Field(targetType = FieldType.STRING)
    private UUID processId;
    private ProcessType processType;
    private EntityType entityType;
    private List<DataRecord> dataRecords;
    private ProcessStatus status;
    private Map<String, String> extraData;
}
