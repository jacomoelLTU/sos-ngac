package ai.aitia.sos_ngac.resource_consumer;

//A class that stands for generall objects. This could be a sensor, a car... This can be built on 
//to make more indepths objects via java heritage. 
public class object {
    private String type;
    private String name;
    private String location;

    //Constructor that creates object with type and name. 
    public object(String type, String name, String location){
        this.type     = type;
        this.name     = name;
        this.location = location;
        
    }
    
    public void setType(String type){
        this.type = type;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setLocation(String location){
        this.location = location;
    }

    
    public String getType(){
        return this.type;
    }
    public String getName(){
        return this.name;
    }
    public String getLocation(){
        return this.location;
    }

}
