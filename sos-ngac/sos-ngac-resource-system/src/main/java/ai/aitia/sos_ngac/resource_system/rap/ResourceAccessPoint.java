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
	 * sent from a consumer and returns a generated resource responseDTO based on
	 * the request operation
	 */
	// TODO: Change return type
	public void access(ResourceRequestDTO requestDTO) {

		// Connect to database
		InfluxDB influxDB = connect();
		
		handleRequest(influxDB, requestDTO);
	}

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
	private void handleRequest(InfluxDB influxDB, ResourceRequestDTO dto) {
		switch (dto.getOperation()) {
		case RAPConstants.READ_OPERATION:
			if (dto.getCondition() != null) {
				// TODO: Implement conditional read
			} else {
				String queryString = queryStringBuilder.userReadAll(dto.getUser(), dto.getObject());
				sendSelectQuery(influxDB, queryString);
			}
			break;
		case RAPConstants.WRITE_OPERATION:
			/*
			 * Time condition for write operations is irrelevant in the 
			 * demo case where sensors write data in real-time. 
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
			
			break;
		}
		//throw new UnsupportedOperationException("Unsupported operation, supported: 'r', 'w'");
	}
	
	// Send select query to influx database and return the response
	public void sendSelectQuery(InfluxDB db, String queryString) {
		QueryResult queryResult = db.query(new Query(queryString, RAPConstants.DATABASE_NAME));
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<SensorMeasurement> memoryPointList = resultMapper.toPOJO(queryResult, SensorMeasurement.class);
		for (SensorMeasurement s : memoryPointList) {
			System.out.println(s.getTime() + " " + s.getName() + " " + s.getObject() + " " + s.getValue());
		}

	}

}
