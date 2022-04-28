package ai.aitia.sos_ngac.resource_system.rap;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ai.aitia.sos_ngac.resource_system.pep.ResourceRequestDTO;


/* 
 * Resource access point class for influxDB interactions 
 */

@Component
public class ResourceAccessPoint {

	@Autowired
	QueryStringBuilder queryStringBuilder;
	

	/*
	 * Entry function for the resource access point. Takes a resource request DTO
	 * sent from a consumer and returns the resource system response
	 */
	public String[] access(ResourceRequestDTO requestDTO) {

		// Connect to database
		InfluxDB influxDB = connect();
		
		// Handle the request and return server response
		return handleRequest(influxDB, requestDTO);
	}
	
	
	
	
	/* ---------------------------- Assistant methods --------------------------------- */

	// Connect to influxDB and return database instance
	private InfluxDB connect() {

		// Connect with set credentials
		InfluxDB influxDB = InfluxDBFactory.connect(RAPConstants.DATABASE_ADDRESS,
				RAPConstants.ROOT_USERNAME, RAPConstants.ROOT_PASSWORD);

		// Activate logger
		// Deactivated due to sensor frequency
		// influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
		
		return influxDB;
	}

	// Handle the request by evaluating the DTO and querying the database
	private String[] handleRequest(InfluxDB influxDB, ResourceRequestDTO dto) {
		switch (dto.getOperation()) {
		case RAPConstants.READ_OPERATION:
			if (dto.getCondition() != null) {
				String[] filteredCondition = filterCondition(dto.getCondition());
				String queryString = queryStringBuilder.userReadConditional(dto.getObject(), filteredCondition);
				return sendSelectQuery(influxDB, queryString);
			} else {
				String queryString = queryStringBuilder.userReadAll(dto.getObject());
				return sendSelectQuery(influxDB, queryString);
			}
		case RAPConstants.WRITE_OPERATION:
			/*
			 * Time condition for write operations is irrelevant in the 
			 * demo case where users write data in real-time. 
			 * However, it might be relevant for other conditions and 
			 * use cases. If so, add conditional handler inside this 
			 * body and define the behavior accordingly.
			 */
			BatchPoints batchPoints = BatchPoints
				.database(RAPConstants.DATABASE_NAME)
				.build();

			Point p = Point.measurement(RAPConstants.DATABASE_MEASUREMENT)
					  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					  .tag(RAPConstants.MEASUREMENT_OBJECT_FIELD, dto.getObject())
					  .tag(RAPConstants.MEASUREMENT_USER_FIELD, dto.getUser()) 
					  .addField(RAPConstants.MEASUREMENT_VALUE_FIELD, dto.getValue()) 
					  .build();
			batchPoints.point(p);
			influxDB.write(batchPoints);
			
			return new String[] {"Write successful"};
		}
		throw new UnsupportedOperationException("Unsupported operation, supported: 'r', 'w'");
	}
	
	// Send select query to influx database and return the response
	private String[] sendSelectQuery(InfluxDB db, String queryString) {
		QueryResult queryResult = db.query(new Query(queryString, RAPConstants.DATABASE_NAME));
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<SensorMeasurement> sensorPointList = resultMapper.toPOJO(queryResult, SensorMeasurement.class);
		String[] resourceSystemResponse = new String[sensorPointList.size()];
		for (int i = 0; i < resourceSystemResponse.length; i++) {
			SensorMeasurement s = sensorPointList.get(i);
			resourceSystemResponse[i] = "Entry: " + s.getTime() 
			+ " | Source: " + s.getName() 
			+ " | Object: " + s.getObject() 
			+ " | Value: " + s.getValue();
		}
		return resourceSystemResponse;
	}
	
	// Filters time values from time condition string 
	public String[] filterCondition(String conditionString) {
		String filter = (conditionString.replace("time_conditional_read(","")).replace(")", "");
		String[] values = filter.split(",");
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i] + RAPConstants.TO_INFLUX_TIMESTAMP_PRECISION;
		}
		return values;
	}

}
