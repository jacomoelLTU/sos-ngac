package ai.aitia.sos_ngac.resource_system.pep;

import java.io.Serializable;

public class ResourceRequestDTO implements Serializable {

	
	private static final long serialVersionUID = -512686594471209954L;

	private String user;
	private String operation;
	private String object;
	private String value; // Value to write in case or write operation
	private String condition;
	
	// Default constructor for spring boot mapping
	public ResourceRequestDTO() {}
	
	// Constructor for non-conditional request
	public ResourceRequestDTO(String user, String operation, String object, String value) {
		this.user = user;
		this.operation = operation;
		this.object = object;
		this.value = value;
	}
	
	// Constructor for conditional request
	public ResourceRequestDTO(String user, String operation, String object, String value, String condition) {
		this.user = user;
		this.operation = operation;
		this.object = object;
		this.value = value;
		this.condition = condition;
	}
	
	public String getUser() {
		return user;
	}

	public String getOperation() {
		return operation;
	}

	public String getObject() {
		return object;
	}
	
	public String getValue() {
		return value;
	}

	public String getCondition() {
		return condition;
	}

}
