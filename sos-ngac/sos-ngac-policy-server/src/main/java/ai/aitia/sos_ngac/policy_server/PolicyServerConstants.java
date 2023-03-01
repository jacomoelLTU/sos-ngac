package ai.aitia.sos_ngac.policy_server;

public class PolicyServerConstants {
	
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String ADMIN_INTERFACE_SERVICE_DEFINITION = "admin-interface";
	public static final String QUERY_INTERFACE_SERVICE_DEFINITION = "query-interface";

	public static final String ADMIN_INTERFACE_URI = "/pai";
	public static final String QUERY_INTERFACE_URI = "/pqi";


	// our thing 2023 , nor sure if this should be here 
	public static final String QUERY_INTERFACE_SERVICE_UPDATE  = "policy-update";
	// our update service ,  not sure if this should be here
	public static final String QUERY_UPDATE_URI = "/pqu";

	public static final String NGAC_SERVER_QUERY_API = "/pqapi/";
	public static final String NGAC_SERVER_ADMIN_API = "/paapi/";
	public static final String NGAC_SERVER_ADDRESS = "http://127.0.0.1:8001";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	

	private PolicyServerConstants() {
		throw new UnsupportedOperationException();
	}
}
