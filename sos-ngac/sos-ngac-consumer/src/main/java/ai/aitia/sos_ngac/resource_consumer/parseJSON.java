package ai.aitia.sos_ngac.resource_consumer;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.jose4j.json.internal.json_simple.JSONObject;

public class parseJSON {
    private String Jbody;
    
    public parseJSON(String Jbody){
        this.Jbody = Jbody;
    }

    public String getJbody() {
        String parsedBody = this.Jbody;
        //{"id":66,"systemName":"sensor8","address":"localhost","port":0,"metadata":{"SensorType":"Temp","additionalProp3":"string","manF":"BOOSCH"},"createdAt":"2023-02-08T11:04:23Z","updatedAt":"2023-02-08T11:04:23Z"}
        JSONParser parser = new JSONParser(parsedBody);
        try {
            JSONObject obj = (JSONObject) parser.parse();
            parsedBody = (String) obj.get("id");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        

        return parsedBody;
    }    
}


