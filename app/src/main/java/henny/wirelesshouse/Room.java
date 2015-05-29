package henny.wirelesshouse;

import java.util.ArrayList;
import java.util.List;

import henny.wirelesshouse.Appliances.Appliance;
import henny.wirelesshouse.Appliances.Door;
import henny.wirelesshouse.Appliances.Light;
import henny.wirelesshouse.Appliances.Temperature;
import henny.wirelesshouse.Appliances.Window;

/**
 * Created by Henny on 2015-04-01.
 */
public class Room {
    String name;
    String type;
    List<Appliance> appliances = new ArrayList<Appliance>();
    List<String> appliancesNames;
    Door door = new Door("Closed", "Kitchen Door");

    public Room(String name) {
        this.name = name;
    };

    public void setDoor (Door d){
        this.door = d;
    }

    public String getName(){
        return name;
    }

    public List<Appliance> getAppliances(){ return appliances;}

    public void setAppliances(List<Appliance> appliances){
        this.appliances = appliances;
    }

    /*
    Get list of appliances' names
     */
    public List<String> getAppliancesNames() {
        appliancesNames = new ArrayList<String>();
        for (Appliance appliance : appliances) appliancesNames.add(appliance.getName());
        return appliancesNames;
    }

    public void addDoor(Door door){appliances.add(door);}

    public void addTemp(Temperature temp){appliances.add(temp);}

    public void addWindow(Window window){
        appliances.add(window);
    }

    public void addLight(Light light){
        appliances.add(light);
    }




   /*
    Czy potrzebne?

    public String getDoor(){ return door.getState();}

    public String getLight(){
        return light.getState();
    }

    public String getWindow(){return "Open/Closed";};
*/

}
