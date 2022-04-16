package ai.aitia.demo.car_provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import ai.aitia.demo.car_common.dto.PolicyRequestDTO;
import ai.aitia.demo.car_common.dto.PolicyResponseDTO;
import ai.aitia.demo.car_provider.CarProviderConstants;


@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class CarServiceController {
	
	@Autowired
	private ApiHandler apiHandler;

	// -------------------------------------------------------------------------------------------------
	@PostMapping(value = CarProviderConstants.ADMIN_INTERFACE_URI)
	@ResponseBody
	public PolicyResponseDTO pai(@RequestBody final PolicyRequestDTO dto) {
		PolicyResponseDTO respdto = new PolicyResponseDTO("test", "test", "test");
		return respdto;
	}

	@PostMapping(value = CarProviderConstants.QUERY_INTERFACE_URI)
	@ResponseBody
	public PolicyResponseDTO pqi(@RequestBody final PolicyRequestDTO dto) throws Exception {
		PolicyResponseDTO responseDTO = apiHandler.handleRequest(dto, CarProviderConstants.NGAC_SERVER_QUERY_API);
		return responseDTO;
	}
}
