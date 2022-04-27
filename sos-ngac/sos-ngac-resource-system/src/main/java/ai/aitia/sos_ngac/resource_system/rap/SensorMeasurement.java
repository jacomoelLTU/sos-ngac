package ai.aitia.sos_ngac.resource_system.rap;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;


/*
 * InfluxDB sensor measurement for sos_ngac_demo database
 */

@Measurement(name = RAPConstants.DATABASE_MEASUREMENT)
public class SensorMeasurement {
	
	@Column(name = RAPConstants.MEASUREMENT_TIME_FIELD)
	private String time;

	@Column(name = RAPConstants.MEASUREMENT_USER_FIELD)
	private String name;
	
	@Column(name = RAPConstants.MEASUREMENT_OBJECT_FIELD)
	private String object;
	
	@Column(name = RAPConstants.MEASUREMENT_VALUE_FIELD)
	private String value;
	
	
	
	public String[] getAllMeasurementData() {
		return new String[] { time, name, object, value };
	}
	
	public String getTime() {
		return time;
	}
	
	public String getName() {
		return name;
	}
	
	public String getObject() {
		return object;
	}
	
	public String getValue() {
		return value;
	}
	
}
