package henny.wirelesshouse;

import henny.wirelesshouse.Appliances.Temperature;

/**
 * Created by Henny on 2015-04-03.
 * Main control - extends room for a general house control.
 */
public class House extends Room {
    public House(String name) {
        super(name);
        door.setName("Main Door");
    }
}
