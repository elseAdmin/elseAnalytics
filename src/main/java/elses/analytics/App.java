package elses.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import elses.analytics.data.consumer.FirebaseDatabaseConsumer;

@SpringBootApplication
@EnableScheduling
public class App {
	
	@Autowired
	FirebaseDatabaseConsumer consumer;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

	/**
	 * this method initiates the rest call to firebase for fetching records for all
	 * the test cases and print rssi values and metadata for all beacons.
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void consumeFirebaseRecords() {
		consumer.fetchRecordsAllTestCases();
	}
}