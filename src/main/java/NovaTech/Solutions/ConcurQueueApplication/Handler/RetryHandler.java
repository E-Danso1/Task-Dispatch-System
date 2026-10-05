package NovaTech.Solutions.ConcurQueueApplication.Handler;

import NovaTech.Solutions.ConcurQueueApplication.Model.Task;
import NovaTech.Solutions.ConcurQueueApplication.Model.TaskStatus;
import NovaTech.Solutions.ConcurQueueApplication.Queue.TaskQueueManager;
import NovaTech.Solutions.ConcurQueueApplication.Tracker.TaskStatusTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetryHandler {
    private static final Logger logger = LoggerFactory.getLogger(RetryHandler.class);
    private static final int MAX_RETRIES = 3;

    private final TaskQueueManager queueManager;
    private final TaskStatusTracker tracker;

    public RetryHandler(TaskQueueManager queueManager, TaskStatusTracker tracker) {
        this.queueManager = queueManager;
        this.tracker = tracker;
    }

    public void handleFailure(Task task) throws InterruptedException {

        if (task.getRetryCount() < MAX_RETRIES) {

            Task retriedTask = task.withRetryIncremented();

            queueManager.submit(retriedTask);

            tracker.updateStatus(retriedTask.getId(), TaskStatus.SUBMITTED);

            logger.warn("Retrying {} attempt {}/{}", retriedTask.getName(), retriedTask.getRetryCount(),
                    MAX_RETRIES
            );

        } else {

            tracker.updateStatus(task.getId(), TaskStatus.FAILED);

            logger.error("Task {} permanently FAILED after {} retries", task.getName(), MAX_RETRIES);
        }
    }
}
