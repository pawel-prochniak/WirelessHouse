package henny.wirelesshouse.Appliances;

/**
 * Created by Henny on 2015-04-01.
 */
public class WindowBlind extends Window {
    private int blind;

    public WindowBlind(int state, String name, int blind){
        super (state, name);
        this.blind = blind;
    }

    public int getBlind(){ return blind; };

    public void setBlind(int blind){ this.blind=blind; };
}
