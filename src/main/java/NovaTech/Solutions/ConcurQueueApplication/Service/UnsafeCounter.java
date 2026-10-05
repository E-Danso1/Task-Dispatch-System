package NovaTech.Solutions.ConcurQueueApplication.Service;

import org.springframework.stereotype.Component;

@Component
public class UnsafeCounter {

    private int taskProcessedCount = 0;

    public void increment () {
        taskProcessedCount++;
    }

    public int getTaskProcessedCount() {
        return taskProcessedCount;
    }
}
