package elses.analytics.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import elses.analytics.data.consumer.FirebaseDatabaseConsumer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MetricCollectionService {
	@Autowired
	FirebaseDatabaseConsumer consumer;

	@Autowired
	KalmanFilter kalmanFilter;

	@Autowired
	ArmaFilter armaFilter;

	public void getArmaFilterValue(String testCase){
		JSONObject responseJson = consumer.fetchRecords(testCase);
		Map<String, List<Long>> rssiMap = getBeaconRssiMap(responseJson);
		Map<String, Float> armaRssiMap = new HashMap<String, Float>();
		for(Entry<String, List<Long>> e : rssiMap.entrySet()){
			Float kalman = armaFilter.filter(e.getValue());
			armaRssiMap.put(e.getKey(),kalman);
		}
		System.out.println(testCase + " arma avg rssi map : " + armaRssiMap);
	}

	public void getKalmanFilterValue(String testCase){
		JSONObject responseJson = consumer.fetchRecords(testCase);
		Map<String, List<Long>> rssiMap = getBeaconRssiMap(responseJson);
		Map<String, Float> kalmanRssiMap = new HashMap<String, Float>();
		for(Entry<String, List<Long>> e : rssiMap.entrySet()){
			Float kalman = kalmanFilter.filter(e.getValue());
			kalmanRssiMap.put(e.getKey(),kalman);
		}
		System.out.println(testCase + " kalman avg rssi map : " + kalmanRssiMap);
	}

	public void geAverageRssi(String testCase) {
		JSONObject responseJson = consumer.fetchRecords(testCase);
		Map<String, List<Long>> rssiMap = getBeaconRssiMap(responseJson);
		Map<String, Long> avgRssiMap = getAvgRssi(rssiMap);
		System.out.println(testCase + " avg rssi map : " + avgRssiMap);
	}

	public void getMeanRssi(String testCase) {
		JSONObject responseJson = consumer.fetchRecords(testCase);
		Map<String, List<Long>> rssiMap = getBeaconRssiMap(responseJson);
		System.out.println(testCase + " rssi map : " + rssiMap);
		Map<String, Long> consecutiveMeanRssiMap = getMeanConsecutivelyRssi(rssiMap);
		System.out.println(testCase + " consecutive mean rssi map : " + consecutiveMeanRssiMap);
	}

	private Map<String, Long> getMeanConsecutivelyRssi(Map<String, List<Long>> rssiMap) {
		//Map<String, List<Long>> consecutiveMeanRssiMap = new HashMap<String,List<Long>>();
		/*for (Entry<String, List<Long>> e : rssiMap.entrySet()) {
			//long total = 0;
			long size = e.getValue().size();
			for (int i = 0; i < e.getValue().size()-1; i++) {
				e.getValue().set(i, ((e.getValue().get(i) + e.getValue().get(i + 1)) / 2));
			}
			e.getValue().remove(size-1);
			//consecutiveMeanRssiMap.put(e.getKey(), e.getValue());
		}
		return rssiMap;*/
		Map<String, Long> meanRssiMap = new HashMap<String, Long>();
		for(Entry<String, List<Long>> iterator : rssiMap.entrySet()){
			meanRssiMap.put(iterator.getKey(), getGausianMethod(iterator.getValue()));
		}
		return meanRssiMap;
	}

	private Long getGausianMethod(List<Long> rssi){
		if(rssi.size() == 1){
			return rssi.get(0);
		}

		for(int i=0; i<rssi.size()-1; ++i){
			rssi.set(i, (rssi.get(i)+rssi.get(i+1))/2);
		}
		rssi.remove(rssi.size()-1);
//		System.out.println("List :: "+rssi);

		return getGausianMethod(rssi);
	}

	private Map<String, Long> getAvgRssi(Map<String, List<Long>> rssiMap) {
		Map<String, Long> avgRssiMap = new HashMap<String, Long>();
		for (Entry<String, List<Long>> e : rssiMap.entrySet()) {
			long total = 0;
			long size = e.getValue().size();
			for (Long rssi : e.getValue()) {
				total += rssi;
			}
			total = total / size;
			avgRssiMap.put(e.getKey(), total);
		}
		return avgRssiMap;
	}

	private Map<String, List<Long>> getBeaconRssiMap(JSONObject responseJson) {
		JSONObject beacons1 = responseJson.getJSONObject("Beacon 1");
		JSONObject beacons2 = responseJson.getJSONObject("Beacon 2");
		JSONObject beacons3 = responseJson.getJSONObject("Beacon 3");
		JSONObject beacons4 = responseJson.getJSONObject("Beacon 4");
		Map<String, List<Long>> rssiMap = new HashMap<>();
		List<Long> rssiList1 = new ArrayList<Long>();
		List<Long> rssiList2 = new ArrayList<Long>();
		List<Long> rssiList3 = new ArrayList<Long>();
		List<Long> rssiList4 = new ArrayList<Long>();
		beacons1.keySet().forEach(keyStr -> {
			JSONObject keyvalue = beacons1.getJSONObject(keyStr);
			rssiList1.add(keyvalue.getLong("rssi"));
			// System.out.println(keyvalue.get("rssi"));
		});
		rssiMap.put("beacon1", rssiList1);
		beacons2.keySet().forEach(keyStr -> {
			JSONObject keyvalue = beacons2.getJSONObject(keyStr);
			rssiList2.add(keyvalue.getLong("rssi"));
			// System.out.println(keyvalue.get("rssi"));
		});
		rssiMap.put("beacon2", rssiList2);
		beacons3.keySet().forEach(keyStr -> {
			JSONObject keyvalue = beacons3.getJSONObject(keyStr);
			rssiList3.add(keyvalue.getLong("rssi"));
			// System.out.println(keyvalue.get("rssi"));
		});
		rssiMap.put("beacon3", rssiList3);
		beacons4.keySet().forEach(keyStr -> {
			JSONObject keyvalue = beacons4.getJSONObject(keyStr);
			rssiList4.add(keyvalue.getLong("rssi"));
			// System.out.println(keyvalue.get("rssi"));
		});
		rssiMap.put("beacon4", rssiList4);
		return rssiMap;
	}
}
