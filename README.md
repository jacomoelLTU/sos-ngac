# Arrowhead Framework System of Systems NGAC Project (Java Spring-Boot)

## Description
A resource SoS with NGAC enforcement in Eclipse Arrowhead. In the demo, a consumer sends requests to access (read/write) data in a time series database, and is given permission only if the corresponding policy is set in the Policy Machine.

![SoS NGAC UML Component Diagram](doc/Component1.png?raw=true "SoS NGAC UML Component Diagram")


# Setup guide

## Simulated environment (VM-box) 
A VM-box system is provided for getting a quick startup or as an option, if not desided to setup the system yourself. This will be within a folder in a repo called VMBOX. This so if chosen to work within a simulated environment there is an existing system ready to be imported on your own VM-box.  



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

