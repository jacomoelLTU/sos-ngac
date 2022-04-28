# Arrowhead Framework System of Systems NGAC Project (Java Spring-Boot)

## Description
A System of Systems implementation of Next Generation Access Control in the Eclipse Arrowhead framework. TBC...

### Requirements

The project has the following dependencies:
* **JRE/JDK 11** [Download from here](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
* **Maven 3.5+** [Download from here](http://maven.apache.org/download.cgi) | [Install guide](https://www.baeldung.com/install-maven-on-windows-linux-mac)
* **The Open Group's Policy Machine**  [Download from here](https://github.com/esen96/tog-ngac-crosscpp-LTU) | The policy server provider of this project interfaces with the policy machine to enforce access rights and utilizes a local instance of the ngac server. [Setup guide](#ngacserver)   
* **Authorization settings for the demo systems in your local arrowhead database**

  ***By Arrowhead Management Tool***
  - coming soon
  
  ***By Swagger API documentation***
  
  - [Click here](#authorizationsettings) for a detailed description on Swagger API management for this project. 
  
  ***By MySQL queries***
  
  *Intra-Cloud:*
  - Insert a new entry with the consumer details into the `system_` table.
  - Insert a new entry with the IDs of consumer entry, provider entry and the service definition entry into the `authorization_intra_cloud` table.
  - Insert a new entry with the IDs of authorization intra cloud entry and service interface entry into the `authorization_intra_cloud_interface_connection` table.
  
  *Inter-Cloud:*
  - Insert a new entry with the cloud details into the `cloud` table. The `authentication_info` have to be filled out with the gatekeper's public key of the cloud.
  - Insert a new entry with the IDs of the cloud entry, provider entry and the service definition entry into the `authorization_inter_cloud` table.
  - Insert a new entry with the IDs of authorization inter cloud entry and service interface entry into the `authorization_inter_cloud_interface_connection` table.



## Setup guide

### Launching the Arrowhead core systems

<a name="ngacserver" />

### Setting up the NGAC server

<a name="authorizationsettings" />

### Authorization settings

### Running the project
