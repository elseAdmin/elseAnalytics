package elses.analytics.service;

import elses.analytics.data.consumer.FirebaseDatabaseConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class KalmanFilter {
    @Autowired
    FirebaseDatabaseConsumer consumer;

    /**
     * We use a low value for the process noise (i.e. 0.008). We assume that most of the noise is
     * caused by the measurements.
     **/
    private static float PROCESS_NOISE_DEFAULT = 0.008f;

    private float processNoise = PROCESS_NOISE_DEFAULT;
    private long duration;
    private TimeUnit timeUnit;
    private long maximumTimestamp;

    public KalmanFilter() {
    }

    public KalmanFilter(long duration, TimeUnit timeUnit) {
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    public KalmanFilter(long maximumTimestamp) {
        this.maximumTimestamp = maximumTimestamp;
    }

    public KalmanFilter(long duration, TimeUnit timeUnit, long maximumTimestamp) {
        this.duration = duration;
        this.timeUnit = timeUnit;
        this.maximumTimestamp = maximumTimestamp;
    }

    public float filter(List<Long> rssi) {
        // Measurement noise is set to a value that relates to the noise in the actual measurements
        // (i.e. the variance of the RSSI signal).
        float measurementNoise = calculateVariance(rssi);
        // used for initialization of kalman filter
        float meanRssi = calculateMean(rssi);
        return calculateKalmanRssi(rssi, processNoise, measurementNoise, meanRssi);
    }

    private static float calculateKalmanRssi(List<Long> rssi,
                                             float processNoise, float measurementNoise, float meanRssi) {
        float errorCovarianceRssi;
        float lastErrorCovarianceRssi = 1;
        float estimatedRssi = meanRssi;
        for (Long rss : rssi) {
            float kalmanGain = lastErrorCovarianceRssi / (lastErrorCovarianceRssi + measurementNoise);
            estimatedRssi = estimatedRssi + (kalmanGain * (rss - estimatedRssi));
            errorCovarianceRssi = (1 - kalmanGain) * lastErrorCovarianceRssi;
            lastErrorCovarianceRssi = errorCovarianceRssi + processNoise;
        }
        return estimatedRssi;
    }

    public static float calculateMean(List<Long> values) {
        int sum = 0;
        for(Long value : values){
            sum += value;
        }
        return sum / (float) values.size();
    }

    public static float calculateVariance(List<Long> values) {
        float mean = calculateMean(values);
        float squaredDistanceSum = 0;
        for(Long value : values){
            squaredDistanceSum += Math.pow(value - mean, 2);
        }
        int sampleLength = Math.max(values.size() - 1, 1);
        return squaredDistanceSum / sampleLength;
    }
}
