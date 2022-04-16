package ai.aitia.demo.car_consumer;

public class CarConsumerConstants {
	
	// Members
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	
	public static final String ADMIN_INTERFACE_SERVICE_DEFINITION = "admin-interface";
	public static final String QUERY_INTERFACE_SERVICE_DEFINITION = "query-interface";
	public static final String ADMIN_INTERFACE_URI = "/pai";
	public static final String QUERY_INTERFACE_URI = "/pqi";
	public static final String ADMIN_TOKEN = "admin_token";

	
	private CarConsumerConstants() {
		throw new UnsupportedOperationException();
	}

}
