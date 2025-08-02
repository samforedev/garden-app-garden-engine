package org.course.gardenappgardenengine.shared.audit;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ILogPublisher {
    void publish(AuditLog log) throws JsonProcessingException;
}
