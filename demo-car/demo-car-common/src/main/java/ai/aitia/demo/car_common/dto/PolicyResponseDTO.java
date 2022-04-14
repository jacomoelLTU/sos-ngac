package ai.aitia.demo.car_common.dto;

import java.io.Serializable;

public class PolicyResponseDTO implements Serializable {
	
	private static final long serialVersionUID = 1401494041038039731L;
	
	private String respStatus;
	private String respMessage;
	private String respBody;
	
	public PolicyResponseDTO() {}
	
	public PolicyResponseDTO(String respStatus, String respMessage, String respBody) {
		this.respStatus = respStatus;
		this.respMessage = respMessage;
		this.respBody = respBody;
	}
	
	public String getRespStatus() {return respStatus;}
	public String getRespMessage() {return respMessage;}
	public String getRespBody() {return respBody;}
	
	
	public void setRespStatus(String respStatus) {this.respStatus = respStatus;}
	public void setRespMessage(String respMessage) {this.respMessage = respMessage;}
	public void setRespBody(String respBody) {this.respBody = respBody;}
	
}
