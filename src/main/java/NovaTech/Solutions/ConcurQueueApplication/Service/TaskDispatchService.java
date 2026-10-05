package NovaTech.Solutions.ConcurQueueApplication.Service;

import NovaTech.Solutions.ConcurQueueApplication.Consumer.TaskWorker;
import NovaTech.Solutions.ConcurQueueApplication.Handler.RetryHandler;
import NovaTech.Solutions.ConcurQueueApplication.Producer.TaskProducer;
import NovaTech.Solutions.ConcurQueueApplication.Queue.TaskQueueManager;
import NovaTech.Solutions.ConcurQueueApplication.Tracker.TaskStatusTracker;
import org.springframework.boot.web.server.Shutdown;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
public class TaskDispatchService {

    private final TaskQueueManager queueManager;
    private final ExecutorService workerpool;
    private final TaskStatusTracker statusTracker;
    private final SafeCounter counter;
    private final RetryHandler retryHandler;

    public TaskDispatchService(ExecutorService workerpool,
                               TaskQueueManager queueManager,
                               TaskStatusTracker statusTracker,
                               SafeCounter counter,
                               RetryHandler retryHandler) {

        this.queueManager = queueManager;
        this.workerpool = workerpool;
        this.statusTracker = statusTracker;
        this.counter = counter;
        this.retryHandler = retryHandler;
    }

    public void startSystem() {

        // start producers
        Thread producer1 = new Thread(
                new TaskProducer("producer-1", queueManager, statusTracker));

        Thread producer2 = new Thread(
                new TaskProducer("producer-2", queueManager, statusTracker));

        Thread producer3 = new Thread(
                new TaskProducer("producer-3", queueManager, statusTracker));

        producer1.start();
        producer2.start();
        producer3.start();


        // start workers
        for (int i = 0; i < 5; i++) {
            workerpool.submit(new TaskWorker(queueManager, statusTracker, counter, retryHandler));
        }
        System.out.println("ConcurQueue Started Successfully");
    }
}
