package henny.wirelesshouse;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarException;

import henny.wirelesshouse.Appliances.Appliance;
import henny.wirelesshouse.Appliances.Door;
import henny.wirelesshouse.Appliances.Heating;
import henny.wirelesshouse.Appliances.Light;
import henny.wirelesshouse.Appliances.LightBlind;
import henny.wirelesshouse.Appliances.Temperature;
import henny.wirelesshouse.Appliances.Window;
import henny.wirelesshouse.Appliances.WindowBlind;

/**
 * Created by Henny on 2015-04-21.
 * Actually decoder and coder of JSONs basing on appliances' list.
 */
public class JSONDecoder {
    private static final String TYPE = "type";
    private static final String NAME = "name";
    private static final String IS_OPEN = "isOpen";
    private static final String STATE = "state";
    private static final String PARAM = "param";
    private static final String BLIND = "blind";
    private static final String TEMPERATURE = "temperature";
    private static final String NEW_TEMPERATURE = "newTemperature";

    public JSONDecoder(){

    }

    public List<Appliance> decodeRoom(String jsonString, String room){
        List<Appliance> applianceList = new ArrayList<>();
        try{
            JSONObject json = new JSONObject(jsonString);
            JSONArray typeArray = json.getJSONArray(room);
            for (int i = 0; i<typeArray.length(); i++){
                JSONObject t = typeArray.getJSONObject(i);
                String type = t.getString(TYPE);
                JSONObject param = t.getJSONObject(PARAM);
                switch (type){
                    case "door":
                        Door door = new Door();
                        door.setName(param.getString(NAME));
                        door.setState(param.getString(IS_OPEN));
                        applianceList.add(door);
                        break;
                    case "lightBlind":
                        LightBlind blindLight = new LightBlind(
                                param.getBoolean(STATE),
                                param.getString(NAME),
                                param.getInt(BLIND)
                        );
                        applianceList.add(blindLight);
                        break;
                    case "light":
                        Light light = new Light(
                                param.getBoolean(STATE),
                                param.getString(NAME)
                        );
                        applianceList.add(light);
                        break;
                    case "windowBlind":
                        WindowBlind windowBlind = new WindowBlind(
                                param.getInt(STATE),
                                param.getString(NAME),
                                param.getInt(BLIND)
                        );
                        applianceList.add(windowBlind);
                        break;
                    case "window":
                        Window window = new Window(
                                param.getInt(STATE),
                                param.getString(NAME)
                        );
                        applianceList.add(window);
                        break;
                    case "heating":
                        Heating heating = new Heating(
                                param.getInt(TEMPERATURE),
                                param.getInt(NEW_TEMPERATURE)
                        );
                        applianceList.add(heating);
                        break;
                    case "temperature":
                        Temperature temperature = new Temperature( param.getInt(TEMPERATURE));
                        applianceList.add(temperature);
                        break;
            }
            }

        }catch (JSONException e){}
        return applianceList;
    }

    public String encodeRooms(List<Room> roomList) {
        String result = "";
        try {

            JSONObject roomObj = new JSONObject();

            for (Room room : roomList) {
                JSONArray array = new JSONArray();

                List<Appliance> list = room.getAppliances();

                for (Appliance app : list) {
                    JSONObject typeObj = new JSONObject();
                    JSONObject paramObj = new JSONObject();


                    if (app instanceof Door) {
                        Door door = (Door) app;
                        typeObj.put(TYPE, "door");
                        paramObj.put(NAME, door.getName());
                        paramObj.put(IS_OPEN, door.getState());
                    }
                    if (app instanceof Temperature) {
                        if (app instanceof Heating) {
                            Heating heating = (Heating) app;
                            typeObj.put(TYPE, "heating");
                            paramObj.put(NAME, heating.getName());
                            paramObj.put(TEMPERATURE, heating.getTemperature());
                            paramObj.put(NEW_TEMPERATURE, heating.getNewTemperature());
                        } else {
                            Temperature temp = (Temperature) app;
                            typeObj.put(TYPE, "temperature");
                            paramObj.put(NAME, temp.getName());
                            paramObj.put(TEMPERATURE, temp.getState());
                        }
                    }
                    if (app instanceof Window) {
                        if (app instanceof WindowBlind) {
                            WindowBlind window = (WindowBlind) app;
                            typeObj.put(TYPE, "windowBlind");
                            paramObj.put(NAME, window.getName());
                            paramObj.put(STATE, window.getOpen());
                            paramObj.put(BLIND, window.getBlind());
                        } else {
                            Window window = (Window) app;
                            typeObj.put(TYPE, "window");
                            paramObj.put(NAME, window.getName());
                            paramObj.put(STATE, window.getOpen());
                        }
                    }
                    if (app instanceof Light) {
                        if (app instanceof LightBlind) {
                            LightBlind light = (LightBlind) app;
                            typeObj.put(TYPE, "lightBlind");
                            paramObj.put(NAME, light.getName());
                            paramObj.put(STATE, light.getSt());
                            paramObj.put(BLIND, light.getBlind());
                        } else {
                            Light light = (Light) app;
                            typeObj.put(TYPE, "light");
                            paramObj.put(NAME, light.getName());
                            paramObj.put(STATE, light.getSt());
                        }
                    }
                    typeObj.put("param", paramObj);
                    array.put(typeObj);
                }
                switch (room.getName()) {
                    case "Bedroom":
                        roomObj.put("bedroom", array);
                        break;
                    case "Main Control":
                        roomObj.put("main", array);
                        break;
                    case "Kitchen":
                        roomObj.put("kitchen", array);
                        break;
                    case "Living Room":
                        roomObj.put("livingRoom", array);
                        break;
                    case "Bathroom":
                        roomObj.put("bathroom", array);
                        break;
                }
                result = roomObj.toString();

            }
            }catch (JSONException e){result="";}



        //result = out.toString();

        return result;
    }


}

