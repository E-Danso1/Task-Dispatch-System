package NovaTech.Solutions.ConcurQueueApplication.Queue;


import NovaTech.Solutions.ConcurQueueApplication.Model.Task;
import NovaTech.Solutions.ConcurQueueApplication.shutdownManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

@Component
@Slf4j
public class TaskQueueManager {

    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
    private final shutdownManager shutdownManager;


    public TaskQueueManager(shutdownManager shutdownManager) {
        this.shutdownManager = shutdownManager;
    }

    public synchronized void submit(Task task) throws InterruptedException {

        if (shutdownManager.isShuttingDown()) {
            throw new IllegalStateException("System is shutting down.Task is rejected");
        }

        // Queue has space — add the task (PriorityBlockingQueue auto-sorts)
        queue.put(task);

        log.info(" [{}] Task submitted → '{}' | Priority: {} | Queue size: {}",
                Thread.currentThread().getName(),
                task.getName(),
                task.getPriority(),
                queue.size());

        //notifyAll() wakes up any threads waiting in fetch() because the queue was empty.
        notifyAll();

    }

    public Task take() throws InterruptedException {
        return queue.take();

    }
    public void requeue(Task task) {
        queue.put(task);
        log.info("Task re-queued → '{}' | Queue size: {}",
                task.getName(),
                queue.size());

    }
    public List<Task> drainAll() {
        List<Task> remaining = new ArrayList<>();
        int count = queue.drainTo(remaining);
        log.info("Queue drained — {} tasks removed during shutdown", count);
        return remaining;
    }
    public int size() {
        return queue.size();
    }
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
