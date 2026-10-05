package NovaTech.Solutions.ConcurQueueApplication.Monitor;

import NovaTech.Solutions.ConcurQueueApplication.Model.TaskStatus;
import NovaTech.Solutions.ConcurQueueApplication.Queue.TaskQueueManager;
import NovaTech.Solutions.ConcurQueueApplication.Service.SafeCounter;
import NovaTech.Solutions.ConcurQueueApplication.Service.UnsafeCounter;
import NovaTech.Solutions.ConcurQueueApplication.Tracker.TaskStatusTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class QueueMonitor {

    private static final Logger logger =
            LoggerFactory.getLogger(QueueMonitor.class);

    private final TaskQueueManager queueManager;
    private final TaskStatusTracker tracker;
    private final ThreadPoolExecutor executor;
    private final SafeCounter counter;

    QueueMonitor(TaskQueueManager queueManager,
                 TaskStatusTracker tracker,
                 ThreadPoolExecutor executor,
                 SafeCounter counter) {

        this.queueManager = queueManager;
        this.tracker = tracker;
        this.executor = executor;
        this.counter = counter;
    }

    @Scheduled(fixedRate = 5000)
    public void monitorSystem() {
        logger.info("=== SYSTEM SNAPSHOT ===");
        logger.info("queue size : {} ", queueManager.size());
        logger.info("Pool Size: {}", executor.getPoolSize());
        logger.info("Active Threads: {}", executor.getActiveCount());
        logger.info("Completed Tasks: {}", executor.getCompletedTaskCount());
        logger.info("Processed counter: {}", counter.getTaskProcessedCount());
        logger.info("=======================");


        long submitted =
                tracker.getAllStatuses()
                .values()
                .stream()
                .filter(status ->
                        status == TaskStatus.SUBMITTED)
                .count();

        long processing =
                tracker.getAllStatuses()
                        .values()
                        .stream()
                        .filter(status ->
                                status == TaskStatus.PROCESSING)
                        .count();

        long completed =
                tracker.getAllStatuses()
                        .values()
                        .stream()
                        .filter(status ->
                                status == TaskStatus.COMPLETED)
                        .count();

        long failed =
                tracker.getAllStatuses()
                        .values()
                        .stream()
                        .filter(status ->
                                status == TaskStatus.FAILED)
                        .count();


        logger.info("Submitted : {}", submitted);
        logger.info("Processing: {}", processing);
        logger.info("Completed : {}", completed);
        logger.info("Failed    : {}", failed);

        detectStuckTasks();

        logger.info("=========");
    }

    private void detectStuckTasks() {
        for (Map.Entry<UUID, ?> entry :
                tracker.getProcessingTasks().entrySet()) {

            UUID taskId = entry.getKey();

            Instant startedAt =
                    tracker.getProcessingTasks()
                            .get(taskId)
                            .getStartedAt();

            long seconds = Duration.between(startedAt, Instant.now()).toSeconds();

            if (seconds > 10) {
                logger.warn("Task {} appears STUCK. Running {} seconds.", taskId, seconds);
            }
        }
    }
}
