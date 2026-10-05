package NovaTech.Solutions.ConcurQueueApplication.Producer;

public enum ProducerStrategy {

    //submit only urgent,high-priority tasks (priority 8-10)
    HIGH_PRIORITY,

    // submit only low-priority tasks (priority 1-3)
    LOW_PRIORITY,

    // submit random mix of high and low priority tasks
    MIXED
}
