package henny.wirelesshouse.Appliances;

/**
 * Created by Henny on 2015-04-01.
 * Window class
 */
public class Window implements Appliance {
    private int state;
    private String name;

    public Window(int state, String name){
        this.state = state;
        this.name = name;
    }

    public int getStateID(){
        return state;
    }

    @Override
    public String getState(){
        if (state == 0) return "Closed";
        if (state == 1) return "Ajar";
        if (state == 2) return "Open";
        else return "No read";
    }

    @Override
    public String getName(){return name;}


    public void setState(int state){
        this.state=state;
    }


}
