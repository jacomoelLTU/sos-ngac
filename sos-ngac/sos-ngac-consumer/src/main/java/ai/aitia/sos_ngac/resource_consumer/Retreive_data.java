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

/**
 * JavaRestSelect is a simple Java REST client that sends a GET request to a specified URL
 * and retrieves the response in JSON format. The response is then parsed and each JSON object
 * within the array is extracted and its values are printed.
 */
public class JavaRestSelect {

    /**
     * The main method that contains the logic to send a GET request to the specified URL and process the response.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Create an instance of the URL class with the specified URL string
            URL url = new URL("https://localhost:8443/serviceregistry/mgmt/sensor");
            // Open a connection to the URL and retrieve an HttpURLConnection instance
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Set the request method to "GET"
            conn.setRequestMethod("GET");

            // Get the response code from the server
            int responseCode = conn.getResponseCode();

            // If the response code is 200 (OK), retrieve the response from the server
            if (responseCode == 200) {
                // Create a BufferedReader to read the response from the input stream
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                // Read the response line by line and append to the StringBuilder
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                // Close the BufferedReader
                in.close();

                // Parse the response string into a JSONArray
                JSONArray jsonArray = new JSONArray(response.toString());

                // Iterate through the JSON objects in the JSONArray
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    // Extract the values of each JSON object
                    int id = jsonObject.getInt("id");
                    String sensorName = jsonObject.getString("Sensor_Name");
                    String sensorType = jsonObject.getString("Sensor_Type");
                    int temp = jsonObject.getInt("Temprature");
                    boolean isGrp2 = jsonObject.getBoolean("is_Grp2");
                    String location = jsonObject.getString("Location");

                    // Print the extracted values in a formatted string
                    System.out.format("%d, %s, %s, %d, %b, %s\n", id, sensorName, sensorType, temp, isGrp2, location);
                }
            } else {
                // If the response code is not 200, print an error message
                System.err.println("Failed to retrieve data, response code: " + responseCode);
            }
        } catch (Exception e) {
            // If an exception is caught, print an error message and the exception message
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }
}


