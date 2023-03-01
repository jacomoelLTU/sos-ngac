package ai.aitia.sos_ngac.resource_system.pep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.InvalidParameterException;

/* 
 * Policy enforcement point class for consuming policy server query service 
 */

@Component
public class PolicyEnforcementPoint {
	
	// PEP constants
	private final String QUERY_INTERFACE_SERVICE_DEFINITION = "query-interface";
	private final String HTTP_METHOD = "http-method";
	private final String INTERFACE_SECURE = "HTTP-SECURE-JSON";
	private final String INTERFACE_INSECURE = "HTTP-INSECURE-JSON";
	private final String ACCESS_QUERY = "access?";
	private final String CONDITIONAL_ACCESS_QUERY = "caccess?";

	//2023 PEP constatns
	private final String GET_POLICY = "getpol?";
	private final String ADD_ELEMENT = "add?";
	private final String ADD_MULTIPLE_ELEMENTS = "addm?";
	private final String UPDATE_DEFINITION = "policy-update";



	
	@Autowired
	private ArrowheadService arrowheadService;
	
	@Autowired
	protected SSLProperties sslProperties;
	
	private final Logger logger = LogManager.getLogger(PolicyEnforcementPoint.class);

	/* 
	 * Entry function for the policy enforcement point. Sends an 
	 * access request for the given user, object and operation
	 */
	public PolicyResponseDTO accessControl(String user, String operation, String object) {
		
		// Orchestrate the resource system-policy server interaction
		final OrchestrationResultDTO orchestrationResult = orchestrate(QUERY_INTERFACE_SERVICE_DEFINITION);
		
		PolicyRequestDTO dto = new PolicyRequestDTO(ACCESS_QUERY, new String[] {user, operation, object});
		
		// Consume policy server query interface service and get the server response
		PolicyResponseDTO policyServerResponse = consumePQI(orchestrationResult, dto);
		
		return policyServerResponse;
	}

	//entry function for automatic adding 2023 project
	public PolicyResponseDTO accessControlAdd(String user, String operation, String object, String value) {

		System.out.println("NU ÄR VI I accessADD i PEP");
		
		// Orchestrate the resource system-policy server interaction
		final OrchestrationResultDTO orchestrationResult = orchestrate(UPDATE_DEFINITION);
		String op = GET_POLICY;
		String[] args = new String[] {"admin_token"}; 
		switch(operation) {
			case "a":
				op = ADD_ELEMENT;
				args = new String[] {value,"object(s4)","admin_token"};
			break;
		}
		PolicyRequestDTO dto = new PolicyRequestDTO(op, args);
		
		// Consume policy server query interface service and get the server response
		PolicyResponseDTO policyServerResponse = consumePQI(orchestrationResult, dto);
		
		return policyServerResponse;
	}
	
	/* 
	 * Entry function for the policy enforcement point. Sends an access 
	 * request for the given user, object, operation and condition
	 */
	public PolicyResponseDTO accessControl(String user, String operation, String object, String condition) {
		
		// Orchestrate the resource system-policy server interaction
		final OrchestrationResultDTO orchestrationResult = orchestrate(QUERY_INTERFACE_SERVICE_DEFINITION);
		
		PolicyRequestDTO dto = new PolicyRequestDTO(CONDITIONAL_ACCESS_QUERY, new String[] {user, operation, object, condition});
		
		// Consume policy server query interface service and get the server response
		PolicyResponseDTO policyServerResponse = consumePQI(orchestrationResult, dto);
		
		return policyServerResponse;
	}
	
	
	// Arrowhead orchestration function. Returns an arrowhead OrchestrationResultDTO
    private OrchestrationResultDTO orchestrate(final String serviceDefinition) {
    	final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(serviceDefinition)
    			.interfaces(getInterface())
    			.build();
    	
    	final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
    	final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder.requestedService(serviceQueryForm)
    			.flag(Flag.MATCHMAKING, true)
    			.flag(Flag.OVERRIDE_STORE, true)
    			.build();
    	
    	final OrchestrationResponseDTO orchestrationResponse = arrowheadService.proceedOrchestration(orchestrationFormRequest);
    	
    	if (orchestrationResponse == null) {
    		logger.info("No orchestration response received");
    	} else if (orchestrationResponse.getResponse().isEmpty()) {
    		logger.info("No provider found during the orchestration");
    	} else {
    		final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
    		validateOrchestrationResult(orchestrationResult, serviceDefinition);
    		return orchestrationResult;
    	}
    	throw new ArrowheadException("Unsuccessful orchestration: " + serviceDefinition);
    }
    
    // Consume the defined arrowhead service 
    private PolicyResponseDTO consumePQI(final OrchestrationResultDTO orchestrationResult, PolicyRequestDTO requestDTO) {
    	final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
		
		
    	// Consume the service and return the server response
		return arrowheadService.consumeServiceHTTP(PolicyResponseDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(HTTP_METHOD)),
				orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
				getInterface(), token, requestDTO, new String[0]);
    }
    

    // Arrowhead helper function. Gets the right interface depending on the SSL properties of the service
    private String getInterface() {
    	return sslProperties.isSslEnabled() ? INTERFACE_SECURE : INTERFACE_INSECURE;
    }
    
    // Arrowhead helper function. Validates the orchestration process
    private void validateOrchestrationResult(final OrchestrationResultDTO orchestrationResult, final String serviceDefinitin) {
    	if (!orchestrationResult.getService().getServiceDefinition().equalsIgnoreCase(serviceDefinitin)) {
			throw new InvalidParameterException("Requested and orchestrated service definition do not match");
		}
    	
    	boolean hasValidInterface = false;
    	for (final ServiceInterfaceResponseDTO serviceInterface : orchestrationResult.getInterfaces()) {
			if (serviceInterface.getInterfaceName().equalsIgnoreCase(getInterface())) {
				hasValidInterface = true;
				break;
			}
		}
    	if (!hasValidInterface) {
    		throw new InvalidParameterException("Requested and orchestrated interface do not match");
		}
    }
    
}