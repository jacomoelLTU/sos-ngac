package ai.aitia.sos_ngac.resource_consumer;

//This class is defined as other communication DTOs in the system but not used in the same manner,
//this object only hard writes metadata for newly onboarded objects into the system_ table in the arrowhead DB,
//through the processbuilder tool in ConsumerMain.java (postResource method).

//this can for sure be done better with some of the spring api's.

public class ResourcePostDTO {
    private String type;
    private String name;
    private String manufacturer;

    //Constructor that creates object with type and name. 
    public ResourcePostDTO(String type, String name, String manufacturer){
        this.type         = type;
        this.manufacturer = manufacturer;
        this.name         = name;
    }
    
    public void setType(String type){
        this.type = type;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setmanufacturer(String manufacturer){
        this.manufacturer = manufacturer;
    }

    
    public String getType(){
        return this.type;
    }
    public String getName(){
        return this.name;
    }
    public String getmanufacturer(){
        return this.manufacturer;
    }

}
