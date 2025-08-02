package org.course.gardenappgardenengine.domain.models.entity.plant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.course.gardenappgardenengine.domain.models.entity.BaseEntity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "plants")
public class Plant extends BaseEntity {
    @Indexed(unique = true)
    private String code;
    private String name;
    private String description;
    private PlantType plantType;
    private PlantProperties properties;
}
