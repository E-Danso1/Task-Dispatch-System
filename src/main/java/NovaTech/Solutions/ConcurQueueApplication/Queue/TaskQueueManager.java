package NovaTech.Solutions.ConcurQueueApplication.Queue;


import NovaTech.Solutions.ConcurQueueApplication.Task;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.PriorityBlockingQueue;

@Component
@Slf4j
public class TaskQueueManager {

    private int maxCapacity;

    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(20);

    public synchronized void submit(Task task) throws InterruptedException {

        // Bounded queue: block producer if queue is at capacity
        while (queue.size() >= maxCapacity) {
            log.warn("Queue is FULL ({}/{}). Producer [{}] is waiting...",
                    queue.size(), maxCapacity, Thread.currentThread().getName());
            wait();

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
    }
}
