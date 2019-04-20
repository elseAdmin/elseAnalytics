package elses.analytics.data.consumer;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import elses.analytics.constants.ApplicationConstants;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FirebaseDatabaseConsumer {
	public JSONObject fetchRecords(String testCase) {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = ApplicationConstants.urlPreFix + ApplicationConstants.FirebaseProjectId
				+ ApplicationConstants.urlPostFix + testCase +".json";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class);
		JSONObject responseJson = new JSONObject(response.getBody());
		return responseJson;
	}
}
