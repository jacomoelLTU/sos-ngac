# Arrowhead Framework System of Systems NGAC Project (Java Spring-Boot)

## Description
A student project group at *Dept. of Computer Science, Electrical and Space Engineering at Luleå University of Technology course D0020E* contributing to an open source project, Arrowhead Policy Server with automatic data sharing policy assignment for new data sources which is an extensive cloud solution using NGAC to build access policies in large systems. Despite Arrowhead's already well developed platform in the given codebase there was no automation for initialising new objects to an already existing access policy, which the project aimed to develop.

Starting off the group were handed a codebase consisting of the three repos below:

1) A resource SoS with NGAC enforcement in Eclipse Arrowhead, Importing Policy Machine functionalities for NGAC enforcement in Arrowhead SoS. : [https://github.com/esen96/sos-ngac](https://github.com/esen96/sos-ngac) 


2) TOG's implementation of the NGAC Policy Machine with minor modifications from various students at Luleå University of Technology. : [https://github.com/esen96/tog-ngac-crosscpp-LTU](https://github.com/esen96/tog-ngac-crosscpp-LTU)


3) Arrowhead Framework 4.4.0.2 : [https://github.com/eclipse-arrowhead/core-java-spring](https://github.com/eclipse-arrowhead/core-java-spring)

The three repos compiled and run together is represented by the component diagram below, here users can interact with the policy server only through access queries to retrieve data from objects controlled by the policy.

![SoS NGAC UML Component Diagram](doc/Component1.png?raw=true "SoS NGAC UML Component Diagram")

The next diagram shows the datapaths for new services implemented by the group, highlighted in magenta coloured lollipops. See the report pdf for a more elaborated description.

![SoS NGAC UML Updated Component Diagram](doc/UpdatedComponentDiagram.png?raw=true "SoS NGAC UML Updated Component Diagram")

# Requirements

Folow this [link](https://github.com/esen96/sos-ngac#requirements) to find all the required installations to run the system.

## Setup guide alternative 1

Follow the setup guide found in this [repo](https://github.com/esen96/sos-ngac) if you wish manually set up the system. Although this was how the group first proceeded we provide some preferred alterations to this in the next section.

Once everything is installed and set up correctly you can start the system. This should be done in the following order:

1) MYSQL: type "mysql -u root -p" in a terminal

2) INFLUX: type "influx" in a terminal

3) NGAC-SERVER: navigate to the tog-ngac-crosscpp-LTU folder and start the ngac-server by typing "./ngac-server -j"

4) CORESYSTEMS(orchestrator, authorization, service_registry): go to the core-java-spring.../scripts folder and type "./start_coresystems_local.sh" 

5) POLICY-SERVER: navigate to the sos-ngac/sos-ngac/sos-ngac-policy-server/target folder and start the policy server by typing "java -jar sos-ngac-policy-server-4.4.0.2.jar"

6) RESOURCE-SYSTEM: navigate to the sos-ngac/sos-ngac/sos-ngac-resource-system/target folder and start the resource system by typing "java -jar sos-ngac-resource-system-4.4.0.2.jar"

7) POLICY-DEMO: navigate to the tog-ngac-crosscpp-LTU/sos-ngac-demo folder and load a policy for example by typing "./13-2023-ExPolicy-1.sh"

8) RESOURCE-CONSUMER: navigate to the sos-ngac/sos-ngac/sos-ngac-consumer/target folder and start the resource consumer by typing "java -jar sos-ngac-consumer-4.4.0.2.jar" this is where one interacts with the system

## Setup guide alternative 2
A VM-box system is provided for getting a quick startup. Download the OVA file from this MEGA [link](https://mega.nz/folder/wS5ngDRY#PsuiCnkOwguQBoJImDKA_g) found in VM Folder AH Ubuntu 64 bit, which contains our VM clone. Importing this to your virtual environment will run a copy of the virtual environment where the group finished.

Follow the steps in this guide to import it into your virtualbox: [https://medium.com/riow/how-to-open-a-vmdk-file-in-virtualbox-e1f711deacc4](https://medium.com/riow/how-to-open-a-vmdk-file-in-virtualbox-e1f711deacc4)

Once the VM is up and running there are two scripts called **quickStart1.sh** and **quickStart2.sh** these scripts will automatically run the core systems and load the demo policy.

The scripts have a certain order within the **.sh** files, these are in decending order as the commands come. It is important to run these files in order; 1. **quickStart1.sh** 2. **quickStart2.sh**. Both startup scripts will open a set of tabs, enter requested passwords in each tab ordered **left** to **right**. Make sure all **quickStart1.sh** tabs run properly before running **quickStart2.sh**.

**quickStart2.sh** will load the policy in the diagram below, the script that loads the policy can be found [here](https://github.com/jacomoelLTU/tog-ngac-crosscpp-LTU/blob/master/sos-ngac-demo/13-2023-ExPolicy-1.sh). The system setup is now complete and the example policy created for the demo is loaded onto the server, see the guide in the following section on how to use the application.

## Application Demo Guide

The following diagram represents the main use case when a client user wants to add an object to an existing policy, this particular policy reflects access control of two different types of temperature related sensors.

![2023 Example Policy](doc/PolicyExample.png?raw=true "2023 Example Policy")

The client user can add objects to the content of the policy server through the **resource-consumer** via the terminal application. For this purpose choose ```a``` from the first prompted line ```(s/c/a)```, the remaining of the I/O stream is fairly self explanatory.

To try out an access query for the recently added object, rerun the **resource-consumer** and now choose option ```c``` and provide input according to the current policy for a correct test. 

## Manually setting up a service

To set up a new service you have to go through some steps.
First you can implement it in one of the controller files (PolicyServerController, ResourceSystemController) or you could create your own controller class. What you do here is you map a way to reach your wanted service, which is not yet existing.

Actually adding the service is done in MySQL. You add and set up the correct connections there. This has to be done in order. 

MySQL steps in order:
1) Service_definition. You have to add a new service in the service_definition table to be able to register a new service in the service_registry.
2) Service_registry. Here you register the service. The service_id can be extracted from the service_definition table and the system_id is what system the service is requested from.
3) Authorization_intra_cloud. Create access for the service. You have to add the correct consumer and provider IDs according to your service. Interface_id is 1 or 2 which represents SECURE or INSECURE mode respectively.
4) Authorization_intra_cloud_interface_connection. Set up connections for the service.
5) Service_registry_interface_connection. Set up connections for the service.

Here is an example, which actually is used to implement the latest policy update service, you can use this but you do have to change the IDs to match your IDs:

1)
```
INSERT INTO service_definition(service_definition) VALUES ("policy-update");
```
2)
```
INSERT INTO service_registry(service_id, system_id, service_uri, metadata) VALUES (23, 14, "/pu", "http-method=POST");
```
3)
```
INSERT INTO authorization_intra_cloud(consumer_system_id, provider_system_id, service_id) VALUES (13, 14, 23);
```
4)
```
INSERT INTO authorization_intra_cloud_interface_connection(authorization_intra_cloud_id, interface_id) VALUES (4, 2);
```
5)
```
INSERT INTO service_registry_interface_connection(service_registry_id, interface_id) VALUES (630, 2);
```


After the service is created and you have mapped a way to reach it, you have to send a request to use it.
To send a request to the added service you need to first create an orchestration request, get the orchestration result and then send this result with a DTO of some sort, could be a ResourceRequestDTO or a PolicyRequestDTO. To understand this better you can look inside the ConsumerMain file. 

