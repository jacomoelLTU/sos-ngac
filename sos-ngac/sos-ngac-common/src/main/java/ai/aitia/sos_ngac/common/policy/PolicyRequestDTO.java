package ai.aitia.sos_ngac.common.policy;

import java.io.Serializable;


public class PolicyRequestDTO implements Serializable {

	private static final long serialVersionUID = 8483737483858496844L;
	
	private String op;
	private String[] args;
	
	// Default constructor for spring boot mapping
	public PolicyRequestDTO() {}
	
	public PolicyRequestDTO(String op, String[] args) {
		this.op = op;
		this.args = args;
	}

	public String getOp() {
		return op;
	}

	public String[] getArgs() {
		return args;
	}

}
