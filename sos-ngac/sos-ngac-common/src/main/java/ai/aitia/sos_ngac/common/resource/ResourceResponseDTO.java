package ai.aitia.sos_ngac.common.resource;

import java.io.Serializable;

public class ResourceResponseDTO implements Serializable {

	private static final long serialVersionUID = -3006088547668605433L;

	private String serverStatus;
	private String serverMessage;
	private String serverResponseBody;
	private String[] resourceSystemResponse;

	// Default constructor for spring boot mapping
	public ResourceResponseDTO() {
	}

	// Constructor for direct policy response to resource response (operation denied case)
	public ResourceResponseDTO(String serverStatus, String serverMessage, String serverResponseBody) {
		this.serverStatus = serverStatus;
		this.serverMessage = serverMessage;
		this.serverResponseBody = serverResponseBody;
	}

	// Main resource response constructor 
	public ResourceResponseDTO(String serverStatus, String serverMessage, String serverResponseBody, String[] resourceSystemResponse) {
		this.serverStatus = serverStatus;
		this.serverMessage = serverMessage;
		this.serverResponseBody = serverResponseBody;
		this.resourceSystemResponse = resourceSystemResponse;
	}

	public String getServerStatus() {
		return serverStatus;
	}

	public String getServerMessage() {
		return serverMessage;
	}

	public String getServerResponseBody() {
		return serverResponseBody;
	}

	public String[] getResourceSystemResponse() {
		return resourceSystemResponse;
	}
}
