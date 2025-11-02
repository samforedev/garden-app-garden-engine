package org.course.gardenappgardenengine.infraestructure.service.engine;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.course.gardenappgardenengine.application.engine.IEngineConsume;
import org.course.gardenappgardenengine.application.engine.IEntityAssignmentHandler;
import org.course.gardenappgardenengine.domain.models.engine.EngineProcess;
import org.course.gardenappgardenengine.domain.repositories.engine.IEngineProcessRepository;
import org.course.gardenappgardenengine.infraestructure.service.handler.AssignmentHandlerRegistry;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.channels.Channel;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EngineConsume implements IEngineConsume {

    private final IEngineProcessRepository processRepository;
    private final AssignmentHandlerRegistry handlerRegistry;

    @Override
    @RabbitListener(queues = "${rabbitmq.queue}")
    public void consume(String processId) throws Exception {
        UUID processUuid = UUID.fromString(processId);
        EngineProcess process = processRepository.findByProcessId(processUuid)
                .orElseThrow(() -> new RuntimeException("Process not found: " + processId));

        IEntityAssignmentHandler handler = handlerRegistry.getHandler(process.getEntityType());
        if (handler == null)
            throw new IllegalArgumentException("Unknown handler type: " + process.getEntityType());

        handler.process(process.getProcessType(), process.getDataRecords(), process);
    }


    private void handleException(Message message, Channel channel, Message dataMessage, Exception ex) {

    }

}
