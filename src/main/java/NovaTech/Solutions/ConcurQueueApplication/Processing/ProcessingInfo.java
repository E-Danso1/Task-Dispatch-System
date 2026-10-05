package NovaTech.Solutions.ConcurQueueApplication.Processing;

import java.time.Instant;

public class ProcessingInfo {

    private final Instant startedAt;

    public ProcessingInfo(Instant startedAt) {
        this.startedAt = startedAt;
    }
    public Instant getStartedAt() {
        return startedAt;
    }
}
