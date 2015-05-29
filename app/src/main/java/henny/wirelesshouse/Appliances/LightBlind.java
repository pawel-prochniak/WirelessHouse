package henny.wirelesshouse.Appliances;

/**
 * Created by Henny on 2015-04-02.
 */
public class LightBlind extends Light {
    private Integer blind;

    public LightBlind(boolean state, String name, int blind){
        super(state, name);
        this.blind = blind;
    }

    public int getBlind(){
        return blind;
    }
    public void setBlind(int blind) {this.blind = blind;}
}
