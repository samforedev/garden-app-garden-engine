package org.course.gardenappgardenengine.infraestructure.service.handler;

import org.course.gardenappgardenengine.application.engine.IEntityAssignmentHandler;
import org.course.gardenappgardenengine.domain.models.engine.EntityType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AssignmentHandlerRegistry {

    private final Map<EntityType, IEntityAssignmentHandler> handlerMap = new HashMap<>();

    public AssignmentHandlerRegistry(List<IEntityAssignmentHandler> handlers) {
        for (IEntityAssignmentHandler handler : handlers) {
            handlerMap.put(handler.getEntityType(), handler);
        }
    }

    public IEntityAssignmentHandler getHandler(EntityType entityType) {
        return handlerMap.get(entityType);
    }

}
