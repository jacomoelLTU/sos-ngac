/*package ai.aitia.sos_ngac.resource_consumer;


import java.sql.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class JavaMysqlSelect{

    public static void main (String[] args)
}
    try 
    {
        String myDriver =
        String myUrl = new URL("https://localhost:8443/serviceregistry/mgmt%22"); 
        Class.forName(myDriver);
        //change the "" after root to your password that you use to connect to Mysql
        Connection conn = DriverManager.getConnection(myUrl, "root", "");
        //URL mysqlurl = new URL("https://localhost:8443/serviceregistry/mgmt");
		HttpURLConnection connection = (HttpURLConnection) mysqlurl.openConnection();
		System.out.println("Connection established");

        //SQL SELECT query
        String query = "SELECT * FROM sensor";

        //creates the java statement
        Statement st = conn.createStatement();

        //EXECUTE the query and get the java result in termnial
        ResultSet rs = st.executeQuery(query);

        //iterate through the java resultset basically loops through it
        //basically the information we want from the mysql database
        //given from the sensor

        while (rs.next())
        {
            int id = rs.getInt("id");
            String sensorName = rs.getString("Sensor_Name");
            String sensorType = rs.getString("Sensor_Type");
            int temp = rs.getInt("Temprature");
            boolean isGrp2 = rs.getBoolean("is_Grp2");
            String location = rs.getString("Location");

            //print out results
            System.out.format(" %s, %s, %s, %s, %s, %s\n", id, sensorName, sensorType, temp, isGrp2, location);
        }
        //CLOSES CONNECTION
        st.close();
        
    }
    catch (Exception e)
    {
        System.err.println("Got an exception! ");
        System.err.println(e.getMessage());
    
    }*/ //This is connection via JDBC API this other one does the same thing but via RESTful API
package ai.aitia.sos_ngac.resource_consumer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class JavaRestSelect {
    public static void main(String[] args) {
        try {
            URL url = new URL("https://localhost:8443/serviceregistry/mgmt/sensor");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String sensorName = jsonObject.getString("Sensor_Name");
                    String sensorType = jsonObject.getString("Sensor_Type");
                    int temp = jsonObject.getInt("Temprature");
                    boolean isGrp2 = jsonObject.getBoolean("is_Grp2");
                    String location = jsonObject.getString("Location");

                    System.out.format("%d, %s, %s, %d, %b, %s\n", id, sensorName, sensorType, temp, isGrp2, location);
                }
            } else {
                System.err.println("Failed to retrieve data, response code: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}


