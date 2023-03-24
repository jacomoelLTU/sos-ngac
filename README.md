# Arrowhead Framework System of Systems NGAC Project (Java Spring-Boot)

## Description
A student project group at *Dept. of Computer Science, Electrical and Space Engineering at Luleå University of Technology course D0020E* contributing to an open source project Arrowhead Policy Server with automatic data sharing policy assignment for new data sources which is an extensive cloud solution using NGAC to build access policies in large systems. Despite Arrowhead's already well developed platform in the given codebase there was no automation for initialising new objects to an already existing access policy, which the project aimed to develop.

Starting of the group were handed a codebase consisting of the three repos below:

1) A resource SoS with NGAC enforcement in Eclipse Arrowhead, Importing Policy Machine functionalities for NGAC enforcement in Arrowhead SoS. : [https://github.com/esen96/sos-ngac](https://github.com/esen96/sos-ngac) 


2) TOG's implementation of the NGAC Policy Machine with minor modifications from various students at Luleå University of Technology. : [https://github.com/esen96/tog-ngac-crosscpp-LTU](https://github.com/esen96/tog-ngac-crosscpp-LTU)


3) Arrowhead Framework 4.4.0.2 : [https://github.com/eclipse-arrowhead/core-java-spring](https://github.com/eclipse-arrowhead/core-java-spring)

The three repos compiled and run together is represented by the component diagram below, here users can interact with the policy server only through access queries to retrieve data from object controlled by the policy.

![SoS NGAC UML Component Diagram](doc/Component1.png?raw=true "SoS NGAC UML Component Diagram")

<sup>This is a superscript text</sup>



![SoS NGAC UML Component Diagram](doc/UpdatedComponentDiagram.png?raw=true "SoS NGAC UML Component Diagram")

# Requirements

Folow this link [https://github.com/esen96/sos-ngac#requirements](link) to find all the required installations to run the system.

# Setup guide alternative 1

Follow the setup guide found in this [https://github.com/esen96/sos-ngac](repo) if you wish manually set up the syastem. Althoug this was how the group first proceeded we provide some preffer alterations to th
 

# Setup guide alternative 2 (hur kör man ifall man använder vår VM copy)

## Simulated environment (VM-box) 
A VM-box system is provided for getting a quick startup or as an option, if not desided to setup the system yourself. This will be within a folder in a repo called VMBOX. This so if chosen to work within a simulated environment there is an existing system ready to be imported on your own VM-box. 

- emil skriver mera om vad som händer när man kör quickstart 1 och 2, vilka system startas i vilken ordning kort om vad de gör, hur använder man resource consumer för att köra och testa vårat DEMO 

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

