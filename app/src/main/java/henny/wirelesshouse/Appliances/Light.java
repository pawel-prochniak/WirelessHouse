package henny.wirelesshouse.Appliances;

/**
 * Created by Henny on 2015-04-01.
 */
public class Light implements Appliance {
    private String name;
    private boolean state;

    public Light (boolean state, String name){
        this.state = state;
        this.name = name;
    }

    public void changeState(){
        state = !state;
    }

    public boolean isOn(){return state;}

    @Override
    public String getState(){
        if(state) return "On";
        else return "Off";
    }

    @Override
    public String getName() {return name;}

    public void switchLight(){
        changeState();
    }

}
