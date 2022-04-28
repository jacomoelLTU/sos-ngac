package ai.aitia.sos_ngac.resource_system.rap;

import org.springframework.stereotype.Component;


/* 
 * Class for composing select query strings 
 */

@Component
public class QueryStringBuilder {
	
	// Query string for reading object-identified values from a user between to timestamps
	public String userReadConditional(String object, String[] timeConditions) {
		return userReadAll(object) + " AND time >= " + timeConditions[0] + " AND time <= " + timeConditions[1];
	}

	// Query string for reading all object-identified values from a user
	public String userReadAll(String object) {
		return getBaseSelect() + " WHERE " +  specifyObject(object);
	}
	
	// Return object specification substring
	public String specifyObject(String object) {
		return getObjectField() + "=" + wrapString(object);
	}

	// Get the object field of the measurement
	public String getObjectField() {
		return RAPConstants.MEASUREMENT_OBJECT_FIELD;
	}

	// Get base select query string with the set database measurement
	public String getBaseSelect() {
		return "SELECT * FROM " + RAPConstants.DATABASE_MEASUREMENT;
	}

	// Wrap string with string identifier ('')
	public String wrapString(String s) {
		return "'" + s + "'";
	}

}
