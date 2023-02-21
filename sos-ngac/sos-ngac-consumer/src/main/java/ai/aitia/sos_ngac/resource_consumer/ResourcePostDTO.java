package ai.aitia.sos_ngac.resource_consumer;

//A class that stands for generall objects. This could be a sensor, a car... This can be built on 
//to make more indepths objects via java heritage. 
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