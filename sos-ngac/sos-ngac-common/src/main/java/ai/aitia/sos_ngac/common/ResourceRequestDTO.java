package ai.aitia.sos_ngac.common;

import java.io.Serializable;

public class ResourceRequestDTO implements Serializable {

	
	private static final long serialVersionUID = -512686594471209954L;

	private String user;
	private String operation;
	private String object;
	private String condition;
	private boolean conditionSet = false;
	
	// Default constructor for spring boot mapping
	public ResourceRequestDTO() {}
	
	// Constructor for non-conditional request
	public ResourceRequestDTO(String user, String operation, String object) {
		this.user = user;
		this.operation = operation;
		this.object = object;
	}
	
	// Constructor for conditional request
	public ResourceRequestDTO(String user, String operation, String object, String condition) {
		this.user = user;
		this.operation = operation;
		this.object = object;
		this.condition = condition;
		conditionSet = true;
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

	public String getCondition() {
		return condition;
	}
	
	public boolean conditionIsSet() {
		return conditionSet;
	}
}
