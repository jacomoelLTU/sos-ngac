package ai.aitia.sos_ngac.policy_server.controller;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.springframework.stereotype.Component;

import ai.aitia.sos_ngac.common.PolicyOpTable;
import ai.aitia.sos_ngac.common.PolicyRequestDTO;
import ai.aitia.sos_ngac.common.PolicyResponseDTO;
import ai.aitia.sos_ngac.policy_server.PolicyServerConstants;

import static java.util.stream.Collectors.joining;

import java.io.IOException;

@Component
public class ApiHandler {

	public PolicyResponseDTO handleRequest(PolicyRequestDTO requestDTO, String ngacServerApi) throws Exception {
		HashMap<String, String> paramBody = evalRequest(requestDTO);
		URL requestURL = createURL(ngacServerApi, requestDTO.getOp(), paramBody);
		JSONObject serverResponse = sendServerRequest(requestURL);
		PolicyResponseDTO responseDTO = convertToDTO(serverResponse);
		return responseDTO;
	}

	/* 
	 * Cross-checks request DTO with PolicyOpTable and returns a parameter HashMap on valid query.
	 * HashMap structure: { PARAMETER-DEFINITION = parameter from request DTO}
	 * 
	 */
	public HashMap<String, String> evalRequest(PolicyRequestDTO dto) throws Exception {
		String op = dto.getOp();
		String[] args = dto.getArgs();
		String[] operationParams = PolicyOpTable.table.get(dto.getOp());

		if (operationParams.length != args.length) {
			throw new Exception("Invalid parameters for operation: " + op + ". Expected: " + Arrays.toString(operationParams));
		}
		HashMap<String, String> argTable = new HashMap<>();
		for (int i = 0; i < operationParams.length; i++) {
			argTable.put(operationParams[i], args[i]);

		}
		return argTable;
	}
	
	public URL createURL(String serverApi, String op, HashMap<String,String> paramBody) throws MalformedURLException {
		String params = paramBody.entrySet()
                .stream()
                .map(e -> e.getKey() + e.getValue())
                .collect(joining("&"));
		URL url = new URL(PolicyServerConstants.NGAC_SERVER_ADDRESS + serverApi + op + params);
		System.out.println("Generated URL: "+ url);  // TODO: Remove
		return url;
 	}
	
	// Sends a request to the ngac server and returns the server response
	public JSONObject sendServerRequest(URL serverURL) throws IOException, ParseException {
		HttpURLConnection conn = (HttpURLConnection)serverURL.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		
		int responsecode = conn.getResponseCode();
		
		if (responsecode != 200) {
		    throw new RuntimeException("HttpResponseCode: " + responsecode);
		} 
		String inline = "";
		Scanner scanner = new Scanner(serverURL.openStream());
		while (scanner.hasNext()) {
		    inline += scanner.nextLine();
		 }
		 scanner.close();
		 JSONParser parse = new JSONParser();
		 JSONObject response = (JSONObject) parse.parse(inline);
		 System.out.println("RESPONSE: " + response);
		 return response;
	}
	
	// Converts the server JSON response to a PolicyResponseDTO
	public PolicyResponseDTO convertToDTO(JSONObject serverResponse) {
		String respBody = (String) serverResponse.get("respBody");
		String respMessage = (String) serverResponse.get("respMessage");
		String respStatus = (String) serverResponse.get("respStatus");
		return new PolicyResponseDTO(respBody,respMessage,respStatus);
	}
	
	

}
