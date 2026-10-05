package NovaTech.Solutions.ConcurQueueApplication;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class shutdownManager {

    private final AtomicBoolean isShuttingDown = new AtomicBoolean(false);

    public void initiateShutdown() {
        isShuttingDown.set(true);
    }

    public boolean isShuttingDown() {
        return isShuttingDown.get();
    }
}
