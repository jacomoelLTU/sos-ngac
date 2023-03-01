package ai.aitia.sos_ngac.resource_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ai.aitia.sos_ngac.common.resource.ResourceRequestDTO;
import ai.aitia.sos_ngac.common.resource.ResourceResponseDTO;
import ai.aitia.sos_ngac.resource_system.ResourceSystemConstants;
import ai.aitia.sos_ngac.resource_system.pep.PolicyEnforcementPoint;
import ai.aitia.sos_ngac.resource_system.pep.PolicyResponseDTO;
import ai.aitia.sos_ngac.resource_system.rap.RAPConstants;
import ai.aitia.sos_ngac.resource_system.rap.ResourceAccessPoint;

/* 
 * Controller class for mapping resource system provider services
 */

@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ResourceSystemController {

	// Components
	@Autowired 
	private PolicyEnforcementPoint pep;
	
	@Autowired
	private ResourceAccessPoint rap;
	
	
	// Mapping function for resource request service
	@PostMapping(value = ResourceSystemConstants.REQUEST_RESOURCE_URI)
	@ResponseBody
	public ResourceResponseDTO requestResource(@RequestBody final ResourceRequestDTO dto) throws Exception {
		
		System.out.println(dto.getOperation());
		System.out.println("WEEE DONT KNOW A THING");

		String operation = dto.getOperation();
		if(dto.getOperation() == operation){

			System.out.println("inside if");

			PolicyResponseDTO serverResponse;
			serverResponse = pep.accessControlAdd(dto.getUser(), dto.getOperation(), dto.getObject());

			if (serverResponse.getRespMessage().equals(RAPConstants.POLICY_GRANTED)) {
				String[] resourceSystemResponse = rap.access(dto);
				return new ResourceResponseDTO(
						serverResponse.getRespStatus(), 
						serverResponse.getRespMessage(), 
						serverResponse.getRespBody(),
						resourceSystemResponse
						);
			}
			return new ResourceResponseDTO(serverResponse.getRespStatus(), serverResponse.getRespMessage(), serverResponse.getRespBody());
		}

		else {
			PolicyResponseDTO serverResponse;
			if (dto.getCondition() == null) {
				serverResponse = pep.accessControl(dto.getUser(), dto.getOperation(), dto.getObject());
			} else {
				serverResponse = pep.accessControl(dto.getUser(), dto.getOperation(), dto.getObject(), dto.getCondition());
			}
			if (serverResponse.getRespMessage().equals(RAPConstants.POLICY_GRANTED)) {
				String[] resourceSystemResponse = rap.access(dto);
				return new ResourceResponseDTO(
						serverResponse.getRespStatus(), 
						serverResponse.getRespMessage(), 
						serverResponse.getRespBody(),
						resourceSystemResponse
						);
			}
			return new ResourceResponseDTO(serverResponse.getRespStatus(), serverResponse.getRespMessage(), serverResponse.getRespBody());
		} 
	}
}
	

