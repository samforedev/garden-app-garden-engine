package org.course.gardenappgardenengine.shared.audit;

import java.time.LocalDateTime;
import java.util.Map;

public record AuditLog(
        String id,
        String entityId,
        String entity,
        String eventType,
        AuditUser user,
        LocalDateTime timestamp,
        String ipAddress,
        Map<String, String> extraInfo
) {
}
