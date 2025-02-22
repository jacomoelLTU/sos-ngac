package ai.aitia.sos_ngac.resource_consumer;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import ai.aitia.sos_ngac.common.resource.ResourceRequestDTO;
import ai.aitia.sos_ngac.common.resource.ResourceResponseDTO;
import ai.aitia.sos_ngac.common.policy.PolicyRequestDTO;
import ai.aitia.sos_ngac.common.policy.PolicyResponseDTO;
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
import eu.arrowhead.common.exception.ArrowheadException;
import eu.arrowhead.common.exception.InvalidParameterException;

import java.io.*;

/* 
 * Consumer main class which defines all consumer behavior
 */

@SpringBootApplication
@ComponentScan(basePackages = { CommonConstants.BASE_PACKAGE, ConsumerConstants.BASE_PACKAGE })
public class ConsumerMain implements ApplicationRunner {

	// 2023 PEP constants, these are for updating the policy server 
	// without going through the resource system and the PEP components
	private final String HTTP_METHOD = "http-method";
	private final String GET_POLICY = "getpol?";
	private final String ADD_ELEMENT = "add?";
	private final String ADD_MULTIPLE_ELEMENTS = "addm?";
	private final String UPDATE_DEFINITION = "policy-update";

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
		Scanner scanner = new Scanner(System.in); // Start command line

		System.out.println("Choose to start sensor(s) or run resource requests(c) or add a new sensor (a): ");
		System.out.println("Enter option: (s/c/a) >");
		String option = scanner.nextLine();

		if (option.equals("c")) {
			runResourceConsumer(scanner);
		}

		else if (option.equals("s")) {
			scanner.close();
			startSensor();
		}
		// Added alternative a as an option. 2023-jan-16
		else if (option.equals("a")) {
			addSensor(scanner);
		}

		else {
			System.out.println("Invalid command");
		}
		scanner.close();
	}



	// Automated sensor function. Generates and writes data every second
	public void startSensor() throws Exception {
		OrchestrationResultDTO orchestrationResult = orchestrate();
		printOut(orchestrationResult);
		System.out.println("Sensor activated. Outputting values...");
		while (true) {
			Thread.sleep(1000);
			String value = String.valueOf(100.00 + Math.random() * (1.00 - 100.00));
			final ResourceRequestDTO dto = new ResourceRequestDTO("Sensor", "w", "SensorData", value);

			requestResource(orchestrationResult, dto);
		}
	}

	// Runs a command line menu for running resource requests
	public void runResourceConsumer(Scanner scanner) throws Exception {
		OrchestrationResultDTO orchestrationResult = orchestrate();
		while (true) {
			System.out.println("----- Consumer query command line: new request -----");

			System.out.println("Enter username >");
			String name = scanner.nextLine();

			System.out.println("Enter operation (r/w) >");
			String operation = scanner.nextLine();

			if (operation.equals("r")) {

				System.out.println("Enter object name >");
				String object = scanner.nextLine();

				System.out.println("Make conditional request? (y/n) >");
				String conditional = scanner.nextLine();

				if (conditional.equals("y")) {

					System.out.println("Enter readback time (ms) >");
					String millis = scanner.nextLine();

					String readBackTime = String.valueOf(System.currentTimeMillis() - Long.parseLong(millis));
					String currentTime = String.valueOf(System.currentTimeMillis());
					String timeParams = readBackTime + "," + currentTime;

					ResourceRequestDTO dto = new ResourceRequestDTO(name, operation, object, null,
							"time_conditional_read(" + timeParams + ")");
					requestResource(orchestrationResult, dto);
				} else {
					ResourceRequestDTO dto = new ResourceRequestDTO(name, operation, object, null);
					requestResource(orchestrationResult, dto);
				}

			} else if (operation.equals("w")) {

				System.out.println("Enter object name >");
				String object = scanner.nextLine();

				System.out.println("Enter data to write >");
				String value = scanner.nextLine();

				ResourceRequestDTO dto = new ResourceRequestDTO(name, operation, object, value);
				requestResource(orchestrationResult, dto);

			} else {
				System.out.println("Invalid operation, try again");
				scanner.close();
				return;
			}
		}
	}

	// Added the logic behind the choices the user will have, (can be expanded)
	// 2023-jan-16
	public void addSensor(Scanner scanner) throws IOException, InterruptedException, Exception {
		
		// Line below is for the /requestupdate service which are commented on line 212
		// OrchestrationResultDTO orchestrationResult = updateOrchestrate();

		String manuF = "";

		System.out.println("Please enter the sensor type [TempSensor, Thermostat]...");
		String type = scanner.nextLine();

		if (type.equals("TempSensor") || type.equals("Thermostat")) {
			switch (type) {
				case "TempSensor":
					manuF = "BOOSCH";
					break;

				case "Thermostat":
					manuF = "NEST";
					break;
			}
		} else {
			System.out.println("Sensor not supported...");
		}

		System.out.println("Please enter the sensor name..."); // Maybe this could be a drop down menu instead
		String name = scanner.nextLine();

		if (name.length() < 20) {
			Pattern my_pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
			Matcher my_match = my_pattern.matcher(name);
			boolean check = my_match.find();
			if (check) {
				System.out.println("Name is not allowed special chars...");
				return;
			}
		} else {
			System.out.println("Name is invalid...\n");
		}

		//
		ResourcePostDTO dto = new ResourcePostDTO(type, name, manuF);
		postResource(dto);


		// This code can be used to finish implementation of client policy control with security checks. 
		// Calls below are done by using the /requestupdate service and then the /pqu service
		/* 	
			// Get Policy name
			ResourceRequestDTO requestGetpol = new ResourceRequestDTO(null, "g", null, null);
			ResourceResponseDTO result = requestUpdate(orchestrationResult, requestGetpol);

			// Add element request with current policy sent as argument
			ResourceRequestDTO requestUpdate = new ResourceRequestDTO(name, "a", type, result.getServerStatus());
			requestUpdate(orchestrationResult, requestUpdate);

			// Assign added element to type, add api
			ResourceRequestDTO requestUpdateAssign = new ResourceRequestDTO(name, "assign", type, result.getServerStatus());
			requestUpdate(orchestrationResult, requestUpdateAssign);

			//Add and assign element with current policy sent as argument, addm api
			// ResourceRequestDTO requestUpdateAssign = new ResourceRequestDTO(name, "addm", type, result.getServerStatus());
			// requestUpdate(orchestrationResult, requestUpdateAssign);
		*/

		// Get policy and save to variable
		PolicyResponseDTO getpol = requestPU(name, "g", null, null);
		printOut(getpol);

		// Add the new object and edge in the policy server graph, 
		// This is done by two separate add calls, this could also 
		// be done using the addm function from PolicyOpTable instead, see line 354
		requestPU(name, "a", type, getpol.getRespStatus());
		requestPU(name, "assign", type, getpol.getRespStatus());


	}


	// Cosume resource POST from ...
	public void postResource(ResourcePostDTO dto) {
		try {
			System.out.println("---------------Commando come after here--------------");
			String hostName = "http://localhost:8443/serviceregistry/mgmt/systems";
			String application = "accept: application/json";
			String applicationType = "Content-Type: application/json";
			String jsonArr = "{  \"address\": \"localhost\",  \"metadata\": {    \"SensorType\": \"" + dto.getType()
					+ "\",    \"manF\": \"" + dto.getmanufacturer()
					+ "\",    \"additionalProp3\": \"string\"  },  \"port\": 0,  \"systemName\": \"" + dto.getName()
					+ "\"}";

			ProcessBuilder pb = new ProcessBuilder("curl", "-X", "POST", hostName, "-H", application, "-H",
					applicationType, "-d", jsonArr);

			Process process = pb.start();
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			StringBuilder responseStrBuilder = new StringBuilder();

			String line = new String();

			while ((line = br.readLine()) != null) {
				System.out.println("read line from curl command: " + line);
				responseStrBuilder.append(line);
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// Consume resource request from the resource system provider
	public void requestResource(OrchestrationResultDTO orchestrationResult, ResourceRequestDTO dto) {

		logger.info("Resource request: ");
		printOut(dto);
		final String token = orchestrationResult.getAuthorizationTokens() == null ? null
				: orchestrationResult.getAuthorizationTokens().get(getInterface());

		final ResourceResponseDTO providerResponse = arrowheadService.consumeServiceHTTP(ResourceResponseDTO.class,
				HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
				orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(),
				orchestrationResult.getServiceUri(), getInterface(), token, dto, new String[0]);
		logger.info("Provider response");
		printOut(providerResponse);

	}

	// Consume update request from the resource system provider #2023
	public ResourceResponseDTO requestUpdate(OrchestrationResultDTO orchestrationResult, ResourceRequestDTO dto) {
		logger.info("Policy request: ");
		printOut(dto);
		final String token = orchestrationResult.getAuthorizationTokens() == null ? null
				: orchestrationResult.getAuthorizationTokens().get(getInterface());

		final ResourceResponseDTO providerResponse = arrowheadService.consumeServiceHTTP(ResourceResponseDTO.class,
				HttpMethod.valueOf(orchestrationResult.getMetadata().get(ConsumerConstants.HTTP_METHOD)),
				orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(),
				orchestrationResult.getServiceUri(), getInterface(), token, dto, new String[0]);
		logger.info("Provider response");
		printOut(providerResponse);
		return providerResponse;
	}

	// Orchestrate service consumption for requestUpdate #2023
	public OrchestrationResultDTO updateOrchestrate() throws Exception {
		logger.info("Orchestration request for " + ConsumerConstants.QUERY_INTERFACE_SERVICE_UPDATE + " service:");
		final ServiceQueryFormDTO serviceQueryForm = new ServiceQueryFormDTO.Builder(
				ConsumerConstants.QUERY_INTERFACE_SERVICE_UPDATE).interfaces(getInterface()).build();

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
			throw new Exception("No proviđder found during the orchestration");
		}

		final OrchestrationResultDTO orchestrationResult = orchestrationResponse.getResponse().get(0);
		validateOrchestrationResult(orchestrationResult, ConsumerConstants.QUERY_INTERFACE_SERVICE_UPDATE);
		return orchestrationResult;

	}

	//entry function for automatic adding #2023
	public PolicyResponseDTO requestPU(String name, String operation, String type, String value) {
	
		// Orchestrate the resource system-policy server interaction
		final OrchestrationResultDTO orchestrationResult = orchestratePU(UPDATE_DEFINITION);
		String op = GET_POLICY;
		String[] args = new String[] {"admin_token"}; 
		switch(operation) {
			case "a":
				//add
				op = ADD_ELEMENT;
				args = null;
				args = new String[] {value, "object("+name+")" ,"admin_token"};
			break;
			case "assign":
				//assign
				op = ADD_ELEMENT;
				args = null;
				args = new String[] {value, "assign("+name+",'"+type+"')" ,"admin_token"};
			break;
			case "addm":
				op = ADD_MULTIPLE_ELEMENTS;
				args = null;
				args = new String[] {value, "object("+name+") , assign('"+name+"'','"+type+"')" ,"admin_token"};

				// policy_elements=[user(u1),assign('u1','ua1')]
			break;
		}
		PolicyRequestDTO dto = new PolicyRequestDTO(op, args);
		
		// Consume policy server query interface service and get the server response
		PolicyResponseDTO policyServerResponse = consumePU(orchestrationResult, dto);
		
		return policyServerResponse;
	}

	// Arrowhead orchestration function. Returns an arrowhead OrchestrationResultDTO #2023
    private OrchestrationResultDTO orchestratePU(final String serviceDefinition) {
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
	
	// Consume the defined arrowhead service #2023
    private PolicyResponseDTO consumePU(final OrchestrationResultDTO orchestrationResult, PolicyRequestDTO requestDTO) {
    	final String token = orchestrationResult.getAuthorizationTokens() == null ? null : orchestrationResult.getAuthorizationTokens().get(getInterface());
		
		
    	// Consume the service and return the server response
		return arrowheadService.consumeServiceHTTP(PolicyResponseDTO.class, HttpMethod.valueOf(orchestrationResult.getMetadata().get(HTTP_METHOD)),
				orchestrationResult.getProvider().getAddress(), orchestrationResult.getProvider().getPort(), orchestrationResult.getServiceUri(),
				getInterface(), token, requestDTO, new String[0]);
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
			throw new Exception("No proviđder found during the orchestration");
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
