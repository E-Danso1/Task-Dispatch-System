package NovaTech.Solutions.ConcurQueueApplication.Tracker;

import NovaTech.Solutions.ConcurQueueApplication.Model.TaskStatus;
import NovaTech.Solutions.ConcurQueueApplication.Processing.ProcessingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class TaskStatusTracker {

    private final ConcurrentHashMap<UUID, TaskStatus> taskstatuses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, ProcessingInfo> processingTasks = new ConcurrentHashMap<>();

    // put() automatically stores the new status for this task ID
    public void updateStatus(UUID taskId, TaskStatus status) {
        taskstatuses.put(taskId, status);

        if (status == TaskStatus.PROCESSING) {
            processingTasks.put(taskId, new ProcessingInfo(Instant.now()));
        }
        if (status == TaskStatus.COMPLETED || status == TaskStatus.FAILED) {
            processingTasks.remove(taskId);
        }
    }
    public ConcurrentHashMap<UUID, TaskStatus> getAllStatuses() {
        return taskstatuses;
    }

    public ConcurrentHashMap<UUID, ProcessingInfo> getProcessingTasks() {
        return processingTasks;
    }

    public TaskStatus getStatus(UUID taskId) {
        return taskstatuses.get(taskId);
    }



}
