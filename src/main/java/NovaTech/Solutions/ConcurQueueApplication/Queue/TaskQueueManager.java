package NovaTech.Solutions.ConcurQueueApplication.Queue;


import NovaTech.Solutions.ConcurQueueApplication.Task;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.PriorityBlockingQueue;

@Component
@Slf4j
public class TaskQueueManager {

    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>(20) {


    }
}
