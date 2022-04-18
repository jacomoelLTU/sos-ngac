package ai.aitia.sos_ngac.policy_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.sos_ngac.common.PolicyRequestDTO;
import ai.aitia.sos_ngac.common.PolicyResponseDTO;
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
}
