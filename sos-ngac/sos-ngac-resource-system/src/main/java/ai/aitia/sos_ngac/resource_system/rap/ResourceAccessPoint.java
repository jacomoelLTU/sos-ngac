package ai.aitia.sos_ngac.resource_system.rap;

import java.util.List;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Resource access point spring component for influxDB interactions 
@Component
public class ResourceAccessPoint {
	
	@Autowired
	QueryStringBuilder queryStringBuilder;

	// Connect to influxDB
	public void connect() {
		
		// Connect with set credentials
		InfluxDB influxDB = InfluxDBFactory.connect(DatabaseConstants.DATABASE_ADDRESS, DatabaseConstants.ROOT_USERNAME, DatabaseConstants.ROOT_PASSWORD);
		
		// Enable log
		influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
		
		String queryString = queryStringBuilder.userReadAll("hostA");
		QueryResult queryResult = influxDB.query(new Query(queryString, DatabaseConstants.DATABASE_NAME));
		List<List<Object>> values = queryResult.getResults().get(0).getSeries().get(0).getValues();
		for (List<Object> list : values) {
			System.out.println(list);
		}
	}
}
