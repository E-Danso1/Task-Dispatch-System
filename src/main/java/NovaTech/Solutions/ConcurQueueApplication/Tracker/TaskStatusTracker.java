package NovaTech.Solutions.ConcurQueueApplication.Tracker;

import NovaTech.Solutions.ConcurQueueApplication.Task;
import NovaTech.Solutions.ConcurQueueApplication.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TaskStatusTracker {

    private final ConcurrentHashMap<UUID, TaskStatus> taskstatuses = new ConcurrentHashMap<>();

    // put() automatically stores the new status for this task ID
    public void update(UUID taskId, TaskStatus status) {
        taskstatuses.put(taskId, status);
    }

    public TaskStatus getStatus(UUID taskId) {
        return taskstatuses.get(taskId);
    }

    public Map<TaskStatus, Long> countByStatus() {
        return taskstatuses.values()
                .stream()
                .collect(Collectors.groupingBy(
                        status -> status,
                        Collectors.counting()
                ));
    }

    // total number of tasks ever registered
    public int getTotalTracked() {
        return taskstatuses.size();
    }

    public long getProcessingCount() {
        return taskstatuses.values()
                .stream()
                // keep only entries where status == PROCESSING
                .filter(status -> status == TaskStatus.PROCESSING)
                .count(); // count how many matched
    }

    // Print a full status breakdown to the console
    public void logSummary() {
        Map<TaskStatus, Long> counts = countByStatus();

        log.info("Task Status Summary");
        log.info("  Total Tracked : {}", getTotalTracked());

        // For each possible status, print its count (defaulting to 0 if none exist)
        for (TaskStatus status : TaskStatus.values()) {
            // getOrDefault returns 0 if no tasks are in that state yet
            long count = counts.getOrDefault(status, 0L);
            log.info("   {} : {}", status, count);
        }

        log.info("....");
    }
}
