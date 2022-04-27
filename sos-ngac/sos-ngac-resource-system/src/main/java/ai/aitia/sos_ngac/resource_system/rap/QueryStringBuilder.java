package ai.aitia.sos_ngac.resource_system.rap;

import org.springframework.stereotype.Component;


/* 
 * Class for composing select query strings 
 */

@Component
public class QueryStringBuilder {

	// Query string for reading all object-identified values from a user
	public String userReadAll(String user, String object) {
		return getBaseSelect() + " WHERE " + specifyUser(user) + " AND " + specifyObject(object);
	}

	// Return user specification substring
	public String specifyUser(String user) {
		return getUserField() + "=" + wrapString(user);
	}

	// Return object specification substring
	public String specifyObject(String object) {
		return getObjectField() + "=" + wrapString(object);
	}

	// Get the user field of the measurement
	public String getUserField() {
		return RAPConstants.MEASUREMENT_USER_FIELD;
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
