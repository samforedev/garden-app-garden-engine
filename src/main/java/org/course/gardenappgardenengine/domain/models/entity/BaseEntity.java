package org.course.gardenappgardenengine.domain.models.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseEntity {
    @Id
    @Field(targetType = FieldType.STRING)
    private UUID id;
    @Field(targetType = FieldType.STRING)
    private UUID version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Builder.Default
    private StatusEntity status = StatusEntity.ACTIVE;
    @Builder.Default
    private boolean deleted = false;
}
