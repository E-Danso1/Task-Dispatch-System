package NovaTech.Solutions.ConcurQueueApplication.Consumer;

import NovaTech.Solutions.ConcurQueueApplication.Handler.RetryHandler;
import NovaTech.Solutions.ConcurQueueApplication.Model.Task;
import NovaTech.Solutions.ConcurQueueApplication.Model.TaskStatus;
import NovaTech.Solutions.ConcurQueueApplication.Queue.TaskQueueManager;
import NovaTech.Solutions.ConcurQueueApplication.Service.SafeCounter;
import NovaTech.Solutions.ConcurQueueApplication.Service.UnsafeCounter;
import NovaTech.Solutions.ConcurQueueApplication.Tracker.TaskStatusTracker;

import java.time.Instant;
import java.util.Random;

public class TaskWorker implements Runnable{

    private final TaskQueueManager taskQueueManager;
    private final TaskStatusTracker statusTracker;
    private final SafeCounter counter;
    private final RetryHandler retryHandler;

    private final Random random = new Random();

    public TaskWorker (TaskQueueManager taskQueueManager,
                       TaskStatusTracker statusTracker,
                       SafeCounter counter,
                       RetryHandler retryHandler) {

        this.taskQueueManager = taskQueueManager;
        this.statusTracker = statusTracker;
        this.counter = counter;
        this.retryHandler = retryHandler;
    }

    @Override
    public void run() {
        while (true) {

            try {
                Task task = taskQueueManager.take();

                statusTracker.updateStatus(task.getId(), TaskStatus.PROCESSING);

                System.out.println("[" + Instant.now() + "] "
                        + Thread.currentThread().getName()
                        + " processing "
                        + task.getName()
                        + " priority="
                        + task.getPriority()
                );

                Thread.sleep(random.nextInt(5000)+1000);


                // simulate failure
                if (random.nextInt(10) < 2) {
                  //  statusTracker.updateStatus(task.getId(), TaskStatus.FAILED);
                    System.out.println(task.getName() + "FAILED");

                    retryHandler.handleFailure(task);

                }

                else {
                    statusTracker.updateStatus(task.getId(), TaskStatus.COMPLETED);

                    counter.increment();
                    System.out.println(task.getName() + "COMPLETED");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
