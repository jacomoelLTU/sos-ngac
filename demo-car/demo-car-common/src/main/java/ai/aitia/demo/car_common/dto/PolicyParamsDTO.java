package ai.aitia.demo.car_common.dto;

import java.io.Serializable;

public class PolicyParamsDTO implements Serializable {

	private static final long serialVersionUID = -4312205786804754559L;

	private String adminToken;
	private String policy;
	private String policyElement;
	private String[] policyElements;
	private String combinedPolicy;
	private String combined;
	private String policyFile;
	private String policySpec;
	private String condName;
	private String[] condElements;
	private String user;
	private String accessRight;
	private String object;
	private String cond;
	private String[] accessList;
	
	
	
	public String getAdminToken() {
		return adminToken;
	}

	public void setAdminToken(String adminToken) {
		this.adminToken = adminToken;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getPolicyElement() {
		return policyElement;
	}

	public void setPolicyElement(String policyElement) {
		this.policyElement = policyElement;
	}

	public String[] getPolicyElements() {
		return policyElements;
	}

	public void setPolicyElements(String[] policyElements) {
		this.policyElements = policyElements;
	}

	public String getCombinedPolicy() {
		return combinedPolicy;
	}

	public void setCombinedPolicy(String combinedPolicy) {
		this.combinedPolicy = combinedPolicy;
	}

	public String getCombined() {
		return combined;
	}

	public void setCombined(String combined) {
		this.combined = combined;
	}

	public String getPolicyFile() {
		return policyFile;
	}

	public void setPolicyFile(String policyFile) {
		this.policyFile = policyFile;
	}

	public String getPolicySpec() {
		return policySpec;
	}

	public void setPolicySpec(String policySpec) {
		this.policySpec = policySpec;
	}

	public String getCondName() {
		return condName;
	}

	public void setCondName(String condName) {
		this.condName = condName;
	}

	public String[] getCondElements() {
		return condElements;
	}

	public void setCondElements(String[] condElements) {
		this.condElements = condElements;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getAccessRight() {
		return accessRight;
	}

	public void setAccessRight(String accessRight) {
		this.accessRight = accessRight;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getCond() {
		return cond;
	}

	public void setCond(String cond) {
		this.cond = cond;
	}

	public String[] getAccessList() {
		return accessList;
	}

	public void setAccessList(String[] accessList) {
		this.accessList = accessList;
	}

}
