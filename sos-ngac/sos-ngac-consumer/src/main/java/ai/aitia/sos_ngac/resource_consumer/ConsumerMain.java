package ai.aitia.sos_ngac.resource_consumer;

import java.util.Scanner;
import java.util.ArrayList;
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
import ai.aitia.sos_ngac.common.policy.PolicyOpConstants;
import ai.aitia.sos_ngac.common.policy.PolicyRequestDTO;
import ai.aitia.sos_ngac.common.policy.PolicyResponseDTO;
import ai.aitia.sos_ngac.common.resource.ResourceRequestDTO;
import ai.aitia.sos_ngac.common.resource.ResourceResponseDTO;
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

import java.io.*;
import java.lang.*;
import java.util.*;


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
		Scanner scanner = new Scanner(System.in);  // Start command line
		
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
		//Added alternative a as an option. 2023-jan-16
		else if (option.equals("a")) {
	    	addSensor(scanner);
	    }

	    else {
	    	System.out.println("Invalid command");
	    }
	    scanner.close();
	}
	//Added the logic behind the choices the user will have, (can be expanded) 2023-jan-16
	public void addSensor(Scanner scanner) throws IOException, InterruptedException, Exception{
		ArrayList<object> objectList = new ArrayList<object>(); //List with lists of attributes

		System.out.println("Please enter the sensor type [temp, thermostat, camera]..."); //Maybe this could be a drop down menu instead
		String type = scanner.nextLine();
		if(type.equals("temp") || type.equals("thermostat")){
			
			//Room for more implementation
			
		}
		else{
			System.out.println("Sensor not supported...");
		}
		System.out.println("Please enter the sensor name..."); //Maybe this could be a drop down menu instead
		String name = scanner.nextLine();
		if(name.length() < 20){
			Pattern my_pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
			Matcher my_match = my_pattern.matcher(name);
			boolean check = my_match.find();
			if(check){
				System.out.println("Name is not allowed special chars...");
				return;
			}
		}

		//Room for more implementation

		else{
			System.out.println("Name is invalid...\n");
		}
			
			
		
		System.out.println("Please enter the sensor location [zoneA, ZoneB]..."); //Maybe this could be a drop down menu instead
		String location = scanner.nextLine();
		if(location.equals("zoneA") || location.equals("zoneB")){
			
			//Room for more implementation

		}
		else{
			System.out.println("Zone does not exist...\n");
		}
		object newObject = new object(type, name, location);

		//Just a part of testing -----------
		System.out.println("The newObject attributes:");
		objectList.add(newObject);
		System.out.println(objectList.get(0).getType() + " " + objectList.get(0).getName() + " " + objectList.get(0).getLocation());
		// ---------------------------------
		
		String curlCommand = "curl -X POST 'http://localhost:8443/serviceregistry/mgmt/systems' -H  'accept: application/json' -H  'Content-Type: application/json' -d '{  \'address\': \'localhost\',  \'metadata\': {    \'SensorType\': \'Temp\',    \'manF\': \'BOOSCH\',    \'additionalProp3\': \'string\'  },  \'port\': 0,  \'systemName\': \'Sensor4\'}'";
		//ProcessBuilder pb = new ProcessBuilder("curl", curlCommand);
		//pb.directory(new File("/home/emil/Desktop/ArrowheadLTU/sos-ngac/sos-ngac/sos-ngac-consumer/src/main/java/ai/aitia/sos_ngac/resource_consumer/ConsumerMain.java"));
		
		try{
			
			//Process process = pb.start();
			System.out.println("Running???");
			Process process = Runtime.getRuntime().exec(curlCommand);
			System.out.println("Running???");

			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			if((line = reader.readLine()) != null){
			System.out.println(line);
			}
			process.waitFor();
			//System.out.println("\nExited with error code : " + exitCode);
		}catch(IOException e){
			e.printStackTrace();
		}

		
	}
	
	// Automated sensor function. Generates and writes data every second
	public void startSensor() throws Exception {
		OrchestrationResultDTO orchestrationResult = orchestrate();
		printOut(orchestrationResult);
		System.out.println("Sensor activated. Outputting values...");
		while(true) {
			Thread.sleep(1000);
			String value = String.valueOf(100.00 + Math.random() * (1.00 - 100.00));
			final ResourceRequestDTO dto = new ResourceRequestDTO("Sensor", "w", "SensorData", value);
			
			requestResource(orchestrationResult, dto);
		}
	}
	
	// Runs a command line menu for running resource requests
	public void runResourceConsumer(Scanner scanner) throws Exception {
		OrchestrationResultDTO orchestrationResult = orchestrate();
		while(true) {
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
				    
				    ResourceRequestDTO dto = new ResourceRequestDTO(name, operation, object, null, "time_conditional_read(" + timeParams + ")");
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
