package ai.aitia.sos_ngac.resource_consumer;

public class ConsumerConstants {
	
	// Members
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	
	public static final String REQUEST_RESOURCE_SERVICE_DEFINITION = "request-resource";
	public static final String REQUEST_RESOURCE_URI = "/request";

	
	public static final String QUERY_INTERFACE_SERVICE_UPDATE = "request-update";
	public static final String QUERY_UPDATE_URI = "/requestupdate";

	
	private ConsumerConstants() {
		throw new UnsupportedOperationException();
	}

}
