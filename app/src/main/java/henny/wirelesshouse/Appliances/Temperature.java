package henny.wirelesshouse.Appliances;

/**
 * Created by Henny on 2015-04-05.
 */
public class Temperature implements Appliance {
    private final String NAME = new String("Outside");
    protected Integer temperature;

    public Temperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public String getName(){return NAME;}

    @Override
    public String getState() {
        return temperature.toString()+ " \u2103";
    }
}
