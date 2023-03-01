package ai.aitia.sos_ngac.common.policy;

public class PolicyUpdateRequestDTO {
    
    //private static final long serialVersionUID = 8483737483858496844L;

    private String op;
    private String[] args;

    //uppdate requestDTO constructor
    public PolicyUpdateRequestDTO(){}

    public PolicyUpdateRequestDTO(String op, String [] args){
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
