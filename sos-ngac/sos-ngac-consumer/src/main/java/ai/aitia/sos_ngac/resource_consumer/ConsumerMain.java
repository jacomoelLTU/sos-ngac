package ai.aitia.sos_ngac.resource_consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;

import ai.aitia.arrowhead.application.library.ArrowheadService;
import ai.aitia.sos_ngac.common.policy.PolicyOpConstants;
import ai.aitia.sos_ngac.common.policy.PolicyRequestDTO;
import ai.aitia.sos_ngac.common.policy.PolicyResponseDTO;
import ai.aitia.sos_ngac.common.resource.ResourceRequestDTO;
import eu.arrowhead.common.CommonConstants;
import eu.arrowhead.common.SSLProperties;
import eu.arrowhead.common.Utilities;
import eu.arrowhead.common.dto.shared.OrchestrationFlags.Flag;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO;
import eu.arrowhead.common.dto.shared.OrchestrationFormRequestDTO.Builder;
import eu.arrowhead.common.dto.shared.OrchestrationResponseDTO;
import eu.arrowhead.common.dto.shared.OrchestrationResultDTO;
import eu.arrowhead.common.dto.shared.ServiceInterfaceResponseDTO;
import eu.arrowhead.common.dto.shared.ServiceQueryFormDTO;
import eu.arrowhead.common.exception.InvalidParameterException;

/* 
 * Consumer main class which defines all consumer behavior
 */

@SpringBootApplication
@ComponentScan(basePackages = { CommonConstants.BASE_PACKAGE, ConsumerConstants.BASE_PACKAGE })
public class ConsumerMain implements ApplicationRunner {

	// members
	@Autowired
	private ArrowheadService arrowheadService;

	@Autowired
	protected SSLProperties sslProperties;

	private final Logger logger = LogManager.getLogger(ConsumerMain.class);

	// Main function
	public static void main(final String[] args) {
		SpringApplication.run(ConsumerMain.class, args);
	}

	@Override
	public void run(final ApplicationArguments args) throws Exception {
		//OrchestrationResultDTO orchestrationResult = orchestrate();
		//requestResource(orchestrationResult);
		startSensor();
	}
	
	// Automated sensor function. Generates and writes data twice per second
	public void startSensor() throws Exception {
		OrchestrationResultDTO orchestrationResult = orchestrate();
		while(true) {
			
			Thread.sleep(500);
			
			String value = String.valueOf(23.00 + Math.random() * (23.00 - 25.00));
			final ResourceRequestDTO dto = new ResourceRequestDTO("Sensor", "w", "Sensor1Data", value);

			logger.info("Resource request: ");
			printOut(dto);
			final String token = orchestrationResult.getAuthorizationTokens() == null ? null
					: orchestrationResult.getAuthorizationTokens().get(getInterface());

			final PolicyResponseDTO resourceRequest = arrowheadService.consumeServiceHTTP(PolicyResponseDTO.class,
					HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
					orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(),
					orchestrationResult.getServiceUri(), getInterface(), token, dto, new String[0]);
			logger.info("Provider response");
			printOut(resourceRequest);
		}
	}
	

	// Consume resource request from the resource system provider
	public void requestResource(OrchestrationResultDTO orchestrationResult) {

		// Resource request test cases
		final ResourceRequestDTO dto = new ResourceRequestDTO("User", "r", "Sensor1Data", null, "time_conditional_read(2,5)");

		logger.info("Resource request: ");
		printOut(dto);
		final String token = orchestrationResult.getAuthorizationTokens() == null ? null
				: orchestrationResult.getAuthorizationTokens().get(getInterface());

		final PolicyResponseDTO resourceRequest = arrowheadService.consumeServiceHTTP(PolicyResponseDTO.class,
				HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
				orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(),
				orchestrationResult.getServiceUri(), getInterface(), token, dto, new String[0]);
		logger.info("Provider response");
		printOut(resourceRequest);

	}
	
	
	
	/* ----------------------- Assistant methods --------------------- */

	// Orchestrate service consumption
	public OrchestrationResultDTO orchestrate() throws Exception {
		logger.info("Orchestration request for " + ConsumerConstants.REQUEST_RESOURCE_SERVICE_DEFINITION + " service:");
		final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(
				ConsumerConstants.REQUEST_RESOURCE_SERVICE_DEFINITION).interfaces(getInterface()).build();

		final Builder orchestrationFormBuilder = arrowheadService.getOrchestrationFormBuilder();
		final OrchestrationFormRequestDTO orchestrationFormRequest = orchestrationFormBuilder
				.requestedService(serviceQueryForm).flag(Flag.MATCHMAKING, true).flag(Flag.OVERRIDE_STORE, true)
				.build();

		printOut(orchestrationFormRequest);

		final OrchestrationResponseDTO orchestrationResponse = arrowheadService
				.proceedOrchestration(orchestrationFormRequest);

		logger.info("Orchestration response:");
		printOut(orchestrationResponse);

		if (orchestrationResponse == null) {
			throw new Exception("No orchestration response received");
		} else if (orchestrationResponse.getResponse().isEmpty()) {
			throw new Exception("No provider found during the orchestration");
		}

		final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
		validateOrchestrationResult(orchestrationResult, ConsumerConstants.REQUEST_RESOURCE_SERVICE_DEFINITION);
		return orchestrationResult;

	}

	private String getInterface() {
		return sslProperties.isSslEnabled() ? ConsumerConstants.INTERFACE_SECURE : ConsumerConstants.INTERFACE_INSECURE;
	}

	private void validateOrchestrationResult(final OrchestrationResultDTO orchestrationResult,
			final String serviceDefinitin) {
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

	private void printOut(final Object object) {
		System.out.println(Utilities.toPrettyJson(Utilities.toJson(object)));
	}
}
