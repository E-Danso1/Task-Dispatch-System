package NovaTech.Solutions.ConcurQueueApplication;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@ToString
public class Task implements Comparable<Task>{

    private final UUID id;
    private final String name;
    private final int priority;
    private final Instant createdTimestamp;
    private final String payload;
    private volatile int retryCount;



    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }

    public Task incrementRetry() {
        // Build a brand-new Task that is Identical to this one
         //except retryCount is one higher
        return Task.builder()
                .id(this.id)
                .name(this.name)
                .priority(this.priority)
                .createdTimestamp(this.createdTimestamp)
                .payload(this.payload)
                .retryCount(this.retryCount + 1)
                .build();
    }

    public Task createTask(String name, int priority, String payload) {
        return Task.builder()
                .id(UUID.randomUUID())              // always unique
                .name(name)
                .priority(priority)
                .createdTimestamp(Instant.now())    // captured at creation time
                .payload(payload)
                .retryCount(0)                      // fresh tas, no retries yet
                .build();
    }
}
