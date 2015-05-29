package henny.wirelesshouse;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import henny.wirelesshouse.Appliances.Appliance;

public class WirelessHome extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    List<Room> roomList;
    HashMap<String, List<Appliance>> listDataChild;
    private volatile String dlText;
    public House main;
    public Room kitchen;
    public Room livingRoom;
    public Room bedroom;
    public Room bathroom;
    public String url = new String ("http://192.168.1.6:8080/dajpokoj");
    public JSONDecoder jsonDec;
    Resources resources;
    private String json;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dlText = null;
        setContentView(R.layout.activity_wireless_home);


        //TextView determining connection
        final TextView txtStart = (TextView) findViewById(R.id.textView);
        new Downloader(){
            @Override
            protected String doInBackground(String... s){
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                dlText = checkConnection();
                setTextView(txtStart, dlText);
            }
        }.execute(url);

        new Downloader(){
            @Override
            protected String doInBackground(String... s){
                sendJSON("http://192.168.208.50:8080/bierz", fileReader());
                return s[0];
            }

            @Override
            protected void onPostExecute(String result) {
            }
        }.execute(url);


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        //declare rooms
        declareRooms();

        // prepare room list
        prepareRoomList();

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


    }


    /*
    Prepare rooms
     */

    private void declareRooms()
    {
        main = new House("Main Control");
        kitchen = new Room("Kitchen");
        livingRoom = new Room("Living Room");
        bedroom = new Room("Bedroom");
        bathroom = new Room("Bathroom");

        jsonDec = new JSONDecoder();

        new Downloader(){
            @Override
            protected String doInBackground(String...strings){
                return fileReader();
            }
            @Override
            protected void onPostExecute(String result) {
                String json;
                json = result;

                //Main control
                main.setAppliances(jsonDec.decodeRoom(json, "main"));

                //Kitchen
                kitchen.setAppliances(jsonDec.decodeRoom(fileReader(), "kitchen"));

                //Living Room
                livingRoom.setAppliances(jsonDec.decodeRoom(json, "livingRoom"));

                //Bedroom
                bedroom.setAppliances(jsonDec.decodeRoom(json, "bedroom"));

                //Bathroom
                bathroom.setAppliances(jsonDec.decodeRoom(json, "bathroom"));
            }
        }.execute("http://192.168.208.50:8080/get_house");

        kitchen.setAppliances(jsonDec.decodeRoom(fileReader(), "main"));

    }



    /*
     * Preparing rooms' list
     */
    private void prepareRoomList(){
        roomList = new ArrayList<Room>();
        roomList.add(main);
        roomList.add(kitchen);
        roomList.add(livingRoom);
        roomList.add(bedroom);
        roomList.add(bathroom);
    }

    /*
     * Preparing list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Appliance>>();

        for (Room room : roomList){
            listDataHeader.add(room.getName());
            listDataChild.put(room.getName(), room.getAppliances()) ;
        }
    }

    private void customToast(String s){
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void setTxt(String in,String out){
        out = in;
    }

    private String checkConnection() {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return "Connected.";
            } else {
                return "No network connection. Using latest known data.";
            }
    }

    private void setTextView(TextView v, String s){
        v.setText(s);
    }

    private String fileReader(){
        InputStream is;
        resources = getResources();
        int rID = resources.getIdentifier("henny.wirelesshouse:raw/json", null, null);
        is = resources.openRawResource(rID);
        try {
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            os.write(buffer);
            os.close();
            is.close();
            return os.toString();
        }catch (IOException e) {
            customToast("IO Exception in buffer");
            return "IO Exception in buffer";
        }
    }

    //KOPSA SIE
    private void FileSaver() {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(Downloader.downloadJSON("http://192.168.1.1"));
            outputStreamWriter.close();
        }
        catch (IOException e) {
            customToast("File write failed: " + e.toString());
        }
    }




}