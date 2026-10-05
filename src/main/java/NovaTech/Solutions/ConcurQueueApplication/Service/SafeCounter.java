package NovaTech.Solutions.ConcurQueueApplication.Service;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SafeCounter {

    private final AtomicInteger taskProcessedCount = new AtomicInteger();

    public void increment() {
        taskProcessedCount.incrementAndGet();
    }

    public int getTaskProcessedCount() {
        return taskProcessedCount.get();
    }
}
