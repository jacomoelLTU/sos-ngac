package ai.aitia.sos_ngac.resource_system.rap;

public class RAPConstants {
	
	// Policy operation status messages
	public static final String POLICY_GRANTED = "grant";
	public static final String POLICY_DENIED = "deny";

	// Database configuration
	protected static final String DATABASE_ADDRESS = "http://localhost:8086";
	protected static final String ROOT_USERNAME = "root";
	protected static final String ROOT_PASSWORD = "root";
	//protected static final String DATABASE_NAME = "sos_ngac_demo";
	protected static final String DATABASE_NAME = "test"; // TODO: remove
	protected static final String DATABASE_MEASUREMENT = "sensordata";
	
	// NGAC operation identifiers
	protected static final String READ_OPERATION = "r";
	protected static final String WRITE_OPERATION = "w";
	
	// NGAC-to-measurement field translations
	protected static final String MEASUREMENT_TIME_FIELD = "time";
	protected static final String MEASUREMENT_USER_FIELD = "sensor";
	protected static final String MEASUREMENT_OBJECT_FIELD = "identifier";
	protected static final String MEASUREMENT_VALUE_FIELD = "value";
	
	// Miscellaneous
	protected static final String DEFAULT_RETENTION_POLICY = "defaultPolicy";
	protected static final String TO_INFLUX_TIMESTAMP_PRECISION = "000000";
	
	public RAPConstants() {
		throw new UnsupportedOperationException();
	}
}
