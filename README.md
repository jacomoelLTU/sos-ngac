# Arrowhead Framework System of Systems NGAC Project (Java Spring-Boot)

## Description
A student project group at *Dept. of Computer Science, Electrical and Space Engineering at Luleå University of Technology course D0020E* contributing to an open source project Arrowhead Policy Server with automatic data sharing policy assignment for new data sources which is an extensive cloud solution using NGAC to build access policies in large systems. Despite Arrowhead's already well developed platform in the given codebase there was no automation for initialising new objects to an already existing access policy, which the project aimed to develop.

Starting of the group were handed a codebase consisting of the three repos below:

1) A resource SoS with NGAC enforcement in Eclipse Arrowhead, Importing Policy Machine functionalities for NGAC enforcement in Arrowhead SoS. : [https://github.com/esen96/sos-ngac](https://github.com/esen96/sos-ngac) 


2) TOG's implementation of the NGAC Policy Machine with minor modifications from various students at Luleå University of Technology. : [https://github.com/esen96/tog-ngac-crosscpp-LTU](https://github.com/esen96/tog-ngac-crosscpp-LTU)


3) Arrowhead Framework 4.4.0.2 : [https://github.com/eclipse-arrowhead/core-java-spring](https://github.com/eclipse-arrowhead/core-java-spring)

The three repos compiled and run together is represented by the component diagram below, here users can interact with the policy server only through access queries to retrieve data from object controlled by the policy.

![SoS NGAC UML Component Diagram](doc/Component1.png?raw=true "SoS NGAC UML Component Diagram")


![SoS NGAC UML Component Diagram](doc/UpdatedComponentDiagram.png?raw=true "SoS NGAC UML Updated Component Diagram")

# Requirements

Folow this link [https://github.com/esen96/sos-ngac#requirements](link) to find all the required installations to run the system.

# Setup guide alternative 1

Follow the setup guide found in this [https://github.com/esen96/sos-ngac](repo) if you wish manually set up the system. Although this was how the group first proceeded we provide some preferred alterations to this in the next section.

# Setup guide alternative 2 (hur kör man ifall man använder vår VM copy)

## Simulated environment (VM-box) 
A VM-box system is provided for getting a quick startup. Download the OVA file from this repository found in VMBOX folder.

Follow the steps in this guide to import it into your virtualbox: [https://medium.com/riow/how-to-open-a-vmdk-file-in-virtualbox-e1f711deacc4](https://medium.com/riow/how-to-open-a-vmdk-file-in-virtualbox-e1f711deacc4)

Once the VM is up and running there are two scripts called **quickStart1.sh** and **quickStart2.sh** these have been created to make life more easy for the one who wants to quickly get every piece of the system running. 

The scripts have a certain order within the **.sh** files, these are in decending order as the commands come. There is also important to run these files in order; 1. **quickStart1.sh** 2. **quickStart2.sh**. There is also some importance to which tabs should be run and when. But a good rule of thumb would be to run them **left** to **right**. Also while running **quickStart1.sh** one should know that when the coresystem tab is running there should be some delay before starting **quickStart2.sh**, about 30 secconds should be enough. 

When everything is running theese could also be used to get a better understanding of which system depend on another. Note that within both of these files there is filepaths to each file; this is useful and important to know if one would like for another **.pl** to be used. The **.pl** file could be thought as the current policy that we load onto the **policy-server** when running **demo.sh** in **quickStart2.sh**. 

If everything is up and running it should be possible to run the **resource-consumer**. We could then test if our policy have been loaded correctly by interacting with the terminal and in order: 

1. Run as a consumer **C**. 
2. Enter set username from chosen **.pl** file. 
3. Choose **R** (easiest way of checking we've loaded the policy). 
4. Conditional Request choice should be **no** (this is not all the way implemented yet). 

This should give a response in the same terminal window that points towards whatever the policy is loaded correctly. 

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

