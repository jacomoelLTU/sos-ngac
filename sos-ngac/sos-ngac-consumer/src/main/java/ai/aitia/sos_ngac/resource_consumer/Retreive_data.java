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
    
    }
