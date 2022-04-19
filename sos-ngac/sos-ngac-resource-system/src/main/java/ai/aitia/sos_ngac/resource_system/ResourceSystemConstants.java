package ai.aitia.sos_ngac.resource_system;

public class ResourceSystemConstants {
	
	public static final String BASE_PACKAGE = "ai.aitia";
	
	public static final String REQUEST_RESOURCE_SERVICE_DEFINITION = "request-resource";
	public static final String FETCH_RESOURCE_SERVICE_DEFINITION = "fetch-resource";
	public static final String REQUEST_RESOURCE_URI = "/request";
	public static final String FETCH_RESOURCE_URI = "/fetch";
	
	public static final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	public static final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	public static final String HTTP_METHOD = "http-method";
	
	private ResourceSystemConstants() {
		throw new UnsupportedOperationException();
	}

}
