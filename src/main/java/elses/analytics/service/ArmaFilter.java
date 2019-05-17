package elses.analytics.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ArmaFilter {

    private static float DEFAULT_ARMA_FACTOR = 0.95f;

    private float armaRssi;

    public float filter(List<Long> rssi) {
//        List<Long> advertisingPackets = getRecentAdvertisingPackets(beacon);
        //use mean as initialization
//        int[] rssiArray = AdvertisingPacketUtil.getRssisFromAdvertisingPackets(advertisingPackets);
        armaRssi = calculateMean(rssi);
//        float frequency = AdvertisingPacketUtil.getPacketFrequency(rssi.size(), duration, timeUnit);
        float frequency = rssi.size()/1;
        float armaFactor = getArmaFactor(frequency);
        for (Long rss : rssi) {
            addMeasurement(rss, armaFactor);
        }
        return getFilteredRssi();
    }

    public void addMeasurement(Long rssi, float armaFactor) {
        armaRssi = armaRssi - (armaFactor * (armaRssi - rssi));
    }

    public float getFilteredRssi() {
        return armaRssi;
    }

    public static float getArmaFactor(float packetFrequency) {
        //TODO make more robust to different packet frequencies
        float armaFactor = DEFAULT_ARMA_FACTOR;
        if (packetFrequency > 6) {
            armaFactor = 0.1f;
        } else if (packetFrequency > 5) {
            armaFactor = 0.25f;
        } else if (packetFrequency > 4) {
            armaFactor = 0.5f;
        }
        return armaFactor;
    }

    public static float calculateMean(List<Long> values) {
        int sum = 0;
        for(Long value : values){
            sum += value;
        }
        return sum / (float) values.size();
    }
}
