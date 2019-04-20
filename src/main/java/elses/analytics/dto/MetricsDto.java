package elses.analytics.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MetricsDto {
	private String name;
	private int rssi;
	private int distance;
	private int txPower;
	private long timestamp;
}
