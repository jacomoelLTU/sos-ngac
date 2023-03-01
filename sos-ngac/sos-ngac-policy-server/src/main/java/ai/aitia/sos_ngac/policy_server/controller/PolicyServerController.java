package ai.aitia.sos_ngac.policy_server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.sos_ngac.common.policy.PolicyRequestDTO;

import ai.aitia.sos_ngac.common.policy.PolicyResponseDTO;
import ai.aitia.sos_ngac.policy_server.PolicyServerConstants;


/* 
 * Controller class for mapping Policy Server provider services
 */

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PolicyServerController {
	
	@Autowired
	private ApiHandler apiHandler;

	
	@PostMapping(value = PolicyServerConstants.ADMIN_INTERFACE_URI)
	@ResponseBody
	public PolicyResponseDTO pai(@RequestBody final PolicyRequestDTO dto) throws Exception {
		PolicyResponseDTO responseDTO = apiHandler.handleRequest(dto, PolicyServerConstants.NGAC_SERVER_ADMIN_API);
		return responseDTO;
	}

	@PostMapping(value = PolicyServerConstants.QUERY_INTERFACE_URI)
	@ResponseBody
	public PolicyResponseDTO pqi(@RequestBody final PolicyRequestDTO dto) throws Exception {
		PolicyResponseDTO responseDTO = apiHandler.handleRequest(dto, PolicyServerConstants.NGAC_SERVER_QUERY_API);
		return responseDTO;
	}

	// New service that updates the server upon adding of new stuff #2023
	@PostMapping(value = PolicyServerConstants.QUERY_UPDATE_URI)
	@ResponseBody
	public PolicyResponseDTO pqu(@RequestBody final PolicyRequestDTO dto) throws Exception {

		//logic kanske måste in här ELLER att vi skickar hit allt som lagts till precis baserat på queries så vi 
		//ba kan assigna allt rätt av

		//process builder 

			// String hostName = "http://127.0.0.1:8001/paapi/getpol";

			// ProcessBuilder pb = new ProcessBuilder("curl", "--G ", hostName, "--data-urlencode", "token=admin_token");

			// Process process = pb.start();
			// InputStream is = process.getInputStream();
			// InputStreamReader isr = new InputStreamReader(is);
			// BufferedReader br = new BufferedReader(isr);
			// StringBuilder responseStrBuilder = new StringBuilder();

			// String line = new String();

			// while ((line = br.readLine()) != null) {
			// 	System.out.println("read line from curl command: " + line);
			// 	responseStrBuilder.append(line);
			// }

			// //pb ger namnet på policy i line stringen...



		PolicyResponseDTO responseDTO = apiHandler.handleRequest(dto, PolicyServerConstants.NGAC_SERVER_ADMIN_API);
		System.out.println(responseDTO.getRespBody());
		System.out.println("ResponsteDTO");
		return responseDTO;
	}
}
