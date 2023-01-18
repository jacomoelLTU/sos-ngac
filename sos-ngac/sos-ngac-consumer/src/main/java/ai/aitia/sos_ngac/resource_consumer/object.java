package ai.aitia.sos_ngac.resource_consumer;

//A class that stands for generall objects. This could be a sensor, a car... This can be built on 
//to make more indepths objects via java heritage. 
public class object {
    public String type;
    public String name;

    //Constructor that creates object with type and name. 
    public object(String type, String name){
        this.type = type;
        this.name = name;
    }
}
