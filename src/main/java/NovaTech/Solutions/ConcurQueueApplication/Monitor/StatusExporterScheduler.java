package NovaTech.Solutions.ConcurQueueApplication.Monitor;

import NovaTech.Solutions.ConcurQueueApplication.JsonExporter;
import NovaTech.Solutions.ConcurQueueApplication.Tracker.TaskStatusTracker;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatusExporterScheduler {

    private final JsonExporter exporter;
    private final TaskStatusTracker tracker;

    public StatusExporterScheduler(
            JsonExporter exporter,
            TaskStatusTracker tracker) {

        this.exporter = exporter;
        this.tracker = tracker;
    }

    @Scheduled(fixedRate = 60000)
    public void exportStatus() {

        exporter.export(tracker);
    }
}
