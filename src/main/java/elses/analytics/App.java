package elses.analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

import elses.analytics.constants.TestCase;
import elses.analytics.service.MetricCollectionService;

@SpringBootApplication
@EnableScheduling
public class App {
	
	@Autowired
	MetricCollectionService service;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}

	/**
	 * this method initiates the rest call to firebase for fetching records for all
	 * the test cases and print rssi values and metadata for all beacons.
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void consumeFirebaseRecords() {
		service.geAverageRssi(TestCase.allBeaconsAt1mPiledUp);
		service.geAverageRssi(TestCase.allBeaconsAt2mPiledUp);
		service.geAverageRssi(TestCase.allBeaconsAt3mPiledUp);
		
		service.getMeanRssi(TestCase.allBeaconsAt1mPiledUp);
		service.getMeanRssi(TestCase.allBeaconsAt2mPiledUp);
		service.getMeanRssi(TestCase.allBeaconsAt3mPiledUp);
		service.getMeanRssi(TestCase.allBeaconsAt4mPiledUp);

		service.getKalmanFilterValue(TestCase.allBeaconsAt1mPiledUp);
		service.getKalmanFilterValue(TestCase.allBeaconsAt2mPiledUp);
		service.getKalmanFilterValue(TestCase.allBeaconsAt3mPiledUp);

		service.getArmaFilterValue(TestCase.allBeaconsAt1mPiledUp);
		service.getArmaFilterValue(TestCase.allBeaconsAt2mPiledUp);
		service.getArmaFilterValue(TestCase.allBeaconsAt3mPiledUp);

	}
}