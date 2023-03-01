package ai.aitia.sos_ngac.common.policy;

import java.io.Serializable;

public class PolicyUpdateResponseDTO implements Serializable {
    
	//private static final long serialVersionUID = 1401494041038039731L;

	private String respStatus;
	private String respMessage;
	private String respBody;
	
	// Default constructor for spring boot mapping
	public PolicyUpdateResponseDTO() {}

	public PolicyUpdateResponseDTO(String respStatus, String respMessage, String respBody) {
		this.respStatus = respStatus;
		this.respMessage = respMessage;
		this.respBody = respBody;
	}

	public String getRespStatus() {
		return respStatus;
	}

	public String getRespMessage() {
		return respMessage;
	}

	public String getRespBody() {
		return respBody;
	}

}
