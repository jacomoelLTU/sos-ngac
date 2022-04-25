package ai.aitia.sos_ngac.resource_system.rap;

import org.springframework.stereotype.Component;

@Component
public class QueryStringBuilder {

	// Query string for reading all values from a host
	public String userReadAll(String user) {
		return "SELECT * FROM " + DatabaseConstants.DATABASE_MEASUREMENT + " WHERE host = '" + user + "'";
	}
}
