package NovaTech.Solutions.ConcurQueueApplication.Producer;


import NovaTech.Solutions.ConcurQueueApplication.Queue.TaskQueueManager;
import NovaTech.Solutions.ConcurQueueApplication.Model.Task;
import NovaTech.Solutions.ConcurQueueApplication.Model.TaskStatus;
import NovaTech.Solutions.ConcurQueueApplication.Tracker.TaskStatusTracker;
import NovaTech.Solutions.ConcurQueueApplication.shutdownManager;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;

@Data
@Builder
@ToString
public class TaskProducer implements Runnable {

    private final String producerName;
    private final TaskQueueManager queueManager;
    private final TaskStatusTracker statusTracker;

    private final Random random = new Random();

    public TaskProducer(String producerName,
                        TaskQueueManager queueManager,
                        TaskStatusTracker statusTracker) {

        this.producerName = producerName;
        this.queueManager = queueManager;
        this.statusTracker = statusTracker;
    }

    @Override
    public void run() {

        try {

            while (!Thread.currentThread().isInterrupted()) {

                int priority;

                if (producerName.equals("Producer-1")) {
                    priority = 10;
                }
                else if (producerName.equals("Producer-2")) {
                    priority = 5;
                }
                else {
                    priority = random.nextInt(10) + 1;
                }

                Task task = new Task(
                        UUID.randomUUID(),
                        "Task-" + System.currentTimeMillis(),
                        priority,
                        Instant.now(),
                        "Sample Payload",
                        0
                );

                queueManager.submit(task);

                statusTracker.updateStatus(task.getId(), TaskStatus.SUBMITTED);

                System.out.println(producerName + " submitted " + task.getName() + " Priority=" + task.getPriority());

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        }
    }
}
