package ai.aitia.sos_ngac.common;

public class PolicyOpConstants {
	
	/* ---------------- Admin set -------------------*/
	
	// Ops
	public static final String GET_POLICY = "getpol?";
	public static final String SET_POLICY = "setpol?";
	public static final String ADD_ELEMENT = "add?";
	public static final String ADD_MULTIPLE_ELEMENTS = "addm?";
	public static final String DELETE = "delete?";
	public static final String DELETE_MULTIPLE_ELEMENTS = "deletem?";
	public static final String COMBINE_POLICIES = "combinepol?";
	public static final String LOAD_POLICY = "load?";
	public static final String LOAD_POLICY_IMMEDIATE = "loadi?";
	public static final String UNLOAD_POLICY = "unload?";
	public static final String READ_POLICY = "readpol?";
	public static final String LOAD_CONDITION_IMMEDIATE = "loadcondi?";
	public static final String UNLOAD_CONDITION_IMMEDIATE = "unloadcondi?";
	
	// Params
	public static final String PARAM_ADMIN_TOKEN = "token=";
	public static final String PARAM_POLICY = "policy=";
	public static final String PARAM_POLICY_ELEMENT = "policy_element=";
	public static final String PARAM_POLICY_ELEMENTS = "policy_elements=";
	public static final String PARAM_POLICY_1 = "policy1=";
	public static final String PARAM_POLICY_2 = "policy2=";
	public static final String PARAM_POLICY_FILENAME = "policyfile=";
	public static final String PARAM_POLICY_SPECIFIER = "policyspec=";
	public static final String PARAM_COMBINE_POLICY_IDENTIFIER = "combined=";
	
	
	/* ---------------- Query set -------------------*/
	
	// Ops
	public static final String ACCESS_QUERY = "access?";
	public static final String CONDITIONAL_ACCESS_QUERY = "caccess?";
	public static final String MULTIPLE_ACCESS_QUERY = "accessm?";
	public static final String GET_OBJECT_INFO = "getobjectinfo?";
	public static final String USERS_QUERY = "users?";
	
	// Params
	public static final String PARAM_USER = "user=";
	public static final String PARAM_ACCESS_RIGHT = "ar=";
	public static final String PARAM_OBJECT = "object=";
	public static final String PARAM_ACCESS_QUERIES = "access_queries=";
	public static final String PARAM_CONDITION= "cond=";
	

	public PolicyOpConstants() {
		throw new UnsupportedOperationException();
	}
}
