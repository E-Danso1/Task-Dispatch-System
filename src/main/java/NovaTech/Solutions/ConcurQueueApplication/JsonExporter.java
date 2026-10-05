package NovaTech.Solutions.ConcurQueueApplication;

import NovaTech.Solutions.ConcurQueueApplication.Tracker.TaskStatusTracker;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class JsonExporter {
    private final ObjectMapper mapper = new ObjectMapper();

    public void export(TaskStatusTracker tracker) {

        Map<UUID, ?> data = tracker.getAllStatuses();

        String fileName =
                "task-status-" + LocalDateTime.now() + ".json";

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(fileName), data);

    }
}
