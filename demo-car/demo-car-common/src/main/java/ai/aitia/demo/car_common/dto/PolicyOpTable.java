package ai.aitia.demo.car_common.dto;

public class PolicyOpTable {
	
	// Admin set
	public static final String GET_POLICY = "getpol";
	public static final String SET_POLICY = "setpol";
	public static final String ADD_ELEMENT = "add";
	public static final String ADD_MULTIPLE_ELEMENTS = "addm";
	public static final String DELETE = "delete";
	public static final String DELETE_MULTIPLE_ELEMENTS = "deletem";
	public static final String COMBINE_POLICIES = "combinepol";
	public static final String LOAD_POLICY = "load";
	public static final String LOAD_POLICY_IMMEDIATE = "loadi";
	public static final String UNLOAD_POLICY = "unload";
	public static final String READ_POLICY = "readpol";
	public static final String LOAD_CONDITION_IMMEDIATE = "loadcondi";
	public static final String UNLOAD_CONDITION_IMMEDIATE = "unloadcondi";
	
	// Query set
	public static final String ACCESS_QUERY = "access";
	public static final String CONDITIONAL_ACCESS_QUERY = "caccess";
	public static final String MULTIPLE_ACCESS_QUERY = "accessm";
	public static final String GET_OBJECT_INFO = "getobjectinfo";
	public static final String USERS_QUERY = "users";
	

	public PolicyOpTable() {
		throw new UnsupportedOperationException();
	}
}
