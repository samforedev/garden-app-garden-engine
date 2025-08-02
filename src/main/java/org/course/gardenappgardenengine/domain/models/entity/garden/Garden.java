package org.course.gardenappgardenengine.domain.models.entity.garden;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.course.gardenappgardenengine.domain.models.entity.BaseEntity;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "gardens")
public class Garden extends BaseEntity {
    @Indexed(unique = true)
    private String code;
    private String name;
    private String description;
    private GardenProperties properties;
    private List<String> plantsCodes = new ArrayList<>();
}
