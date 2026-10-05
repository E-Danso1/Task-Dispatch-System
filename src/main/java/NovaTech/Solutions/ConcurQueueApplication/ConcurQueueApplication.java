package NovaTech.Solutions.ConcurQueueApplication;

import NovaTech.Solutions.ConcurQueueApplication.Service.TaskDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ConcurQueueApplication implements CommandLineRunner {

	@Autowired
	private TaskDispatchService dispatcher;

    public ConcurQueueApplication(TaskDispatchService dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static void main(String[] args) {
		SpringApplication.run(ConcurQueueApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dispatcher.startSystem();
	}
}

