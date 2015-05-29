package henny.wirelesshouse.Appliances;

/**
 * Created by Henny on 2015-04-01.
 */
public class Heating extends Temperature {
    private Integer newTemperature;
    private final String NAME = "Temperature: ";

    public Heating(int temperature, int newTemperature) {
        super(temperature);
        setNewTemperature(newTemperature);
    }

    @Override
    public String getState() {
        return temperature.toString()+ " \u2103";
    }

    @Override
    public String getName(){return NAME;}

    public int getTemperature(){return temperature;}

    public String getNewTemperature(){return newTemperature.toString()+"\u2103";}

    public void setNewTemperature(int newTemperature){this.newTemperature=newTemperature;}

    private void setTemperature(int set){
        this.newTemperature=set;
    }

}
