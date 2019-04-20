package elses.analytics.data.consumer;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import elses.analytics.constants.ApplicationConstants;
import elses.analytics.constants.TestCase;

@Component
public class FirebaseDatabaseConsumer {
	public void fetchRecordsAllTestCases() {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = ApplicationConstants.urlPreFix + ApplicationConstants.FirebaseProjectId
				+ ApplicationConstants.urlPostFix + TestCase.allBeaconsAt1mPiledUp+".json";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		System.out.print(response.getBody());
	}
}
