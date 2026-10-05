package NovaTech.Solutions.ConcurQueueApplication.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ExecutorConfig {

    @Bean
    public ThreadPoolExecutor workerPool() {

        return (ThreadPoolExecutor)
                Executors.newFixedThreadPool(5);
    }
}
