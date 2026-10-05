package NovaTech.Solutions.ConcurQueueApplication.Model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;


@Getter
@Setter
@Data
@Builder
@ToString
public class Task implements Comparable<Task>{

    private final UUID id;
    private final String name;
    private final int priority;
    private final Instant createdTimestamp;
    private final String payload;
    private int retryCount;


    public Task(UUID id, String name, int priority, Instant createdTimestamp, String payload, int retryCount ) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.createdTimestamp = createdTimestamp;
        this.payload = payload;
        this.retryCount = retryCount;
    }

    public Task withRetryIncremented() {
        return new Task(
                this.id,
                this.name,
                this.priority,
                this.createdTimestamp,
                this.payload,
                this.retryCount + 1
        );
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }
}
