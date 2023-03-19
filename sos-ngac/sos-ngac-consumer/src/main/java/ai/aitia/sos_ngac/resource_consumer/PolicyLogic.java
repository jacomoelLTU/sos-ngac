//Policy LOGIC (put it in consumer main) 
        /*  public void getMetadata(ResourceGetDTO dto) {
			try {
				// Set the URL of the service registry to retrieve metadata from
				String hostName = "http://localhost:8443/serviceregistry/mgmt/systems/" + dto.getSystemId();
		
				// Set the Accept and Content-Type headers for the HTTP request
				String application = "accept: application/json";
				String applicationType = "Content-Type: application/json";
		
				// Specify the metadata fields to retrieve as a JSON array
				String jsonArr = "{ \"id\": \"metadata\": [\"SensorType\", \"manF\"]}";
		
				// Construct a ProcessBuilder command to execute the HTTP request using curl
				ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", hostName, "-H", application, "-H",
						applicationType, "-d", jsonArr);
		
				// Start the curl command process and read the output
				Process process = pb.start();
				InputStream is = process.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				StringBuilder responseStrBuilder = new StringBuilder();
		
				String line = new String();
		
				while ((line = br.readLine()) != null) {
					// Print each line of the curl command output
					System.out.println("read line from curl command: " + line);
					responseStrBuilder.append(line);
				}
		
			} catch (IOException e) {
				// Handle any errors that occur during the HTTP request
				e.printStackTrace();
			}
		}*/
  
   
    public static int Logic(int ID, ResourcePostDTO dto){                        //meant to match up new object with one already existing for which the metadata
        String[] newObjectMetadata = parseMetadata(ID, dto);
        int bestMatchID = -1;
        for (int i = ID-1; i >=142; i--){						// 142 is hardcoded bc its a slow implementation generally, can be changed
            String[] array = parseMetadata(i, dto);                  //assumes that objectIDs are incremental, loops through all previously added objects
            int previousBest = -1;
            for(int j = newObjectMetadata.length-1; j >=0; j--){        //checks if the current metadata field matches between the new object and the one its checked
                int counterNew = 0;
                if(newObjectMetadata[j].equals(array[j])){                  
                    counterNew++;                                       //increases counter for how many fields match
                }
                else{
                    continue;
                }
                if(counterNew>previousBest){
                    bestMatchID = i;                        //if the currently checked against object has more matching fields than the previous best,
                                                            //we switch so that BestMatchID always holds the objectID with the most metadata in common with the new sensor
                    previousBest = counterNew;
                }
            }
        }
        return(bestMatchID);   //do something with the fact that we now know the ID for the existing object with the best matching metadat



        // Parse the assign, ascociate and connector commands
             
        // \n  object_attribute('TempSensor'),\n  object_attribute('Thermostat'),\n  policy_class(access),\n  connector('PM'),\n  assign('User1','Group1'),\n  assign('User2','Group1'),\n  assign('User3','Group2'),
        // \n  assign('S1','TempSensor'),\n  assign('S2','TempSensor'),\n  assign('S3','Thermostat'),\n  assign('Group1',access),\n  assign('Group2',access),\n  assign('TempSensor',access),\n  assign('Thermostat',access),
        // \n  assign(access,'PM'),\n  associate('Group1',[r],'TempSensor'),\n  associate('Group2',[r,w],'Thermostat')\n]).\n"
       
        // SEN BYGGA ETT TRÄD UTAV DETTA om ni vill inte måste!! Så den inte fuckas om vi kör typ 100 nodes jämfört med node n

        //use mysql to get jsonbody  


    }

    // Help Function
    public static String[] parseMetadata(int ID, ResourcePostDTO dto){
		String hostName = "http://localhost:8443/serviceregistry/mgmt/systems";
		String application = "accept: application/json";
		String applicationType = "Content-Type: application/json";
		String jsonArr = "{  \"metadata\": {    \"SensorType\": \"" + dto.getType()
					+ "\",    \"manF\": \"" + dto.getmanufacturer()
					+ "\",    \"additionalProp3\": \"string\"  },  \"port\": 0,  \"systemName\": \"" + dto.getName()
					+ "\"}";

        ProcessBuilder pb = new ProcessBuilder("curl", "-X", "GET", hostName, "-H", application, "-H",
		applicationType, "-d", jsonArr);

		Process process = pb.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		StringBuilder responseStrBuilder = new StringBuilder();

		String jsonBody = new String();
		                                                //"{\"metadata\":{\"SensorType\":\"Temp\",\"additionalProp3\":\"string\",\"manF\":\"BOOSCH\"}";
        ArrayList<String> metadataList = new ArrayList<>();

        // Remove curly braces and quotes from JSON string
        String strippedJson = jsonBody.replaceAll("[{}\"]", "");

        // Split the string into key-value pairs
        String[] pairs = strippedJson.split(",");

        //parse jsonbody
 /*     String sensorType = "";
        String additionalProp3 = "";
        String manF = "";*/
       
        int i = 0;
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

 /*            switch (key) {
                case "SensorType":
                    sensorType = value;
                    break;
                case "additionalProp3":
                    additionalProp3 = value;
                    break;
                case "manF":
                    manF = value;
                    break;
            }*/
            metadataList.add(value);
        }

        //return string array with metadata
        String[] array = metadataList.toArray(new String[metadataList.size()]);
        return array;
	}
}

// Read Policyn och Ge en typ
//curl -s -G "http://127.0.0.1:8001/paapi/readpol" --data-urlencode "policy=2023ExP1" --data-urlencode "token=admin_token"
// sen parse output
// compare metadata
// send location to type 
// eller ta utt select all from mysql genom process builder select metadata from system_;
// compare type and manuf which is the metadata
