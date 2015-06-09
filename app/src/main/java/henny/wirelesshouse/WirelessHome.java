package henny.wirelesshouse;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    volatile List<Room> roomList;
    HashMap<String, List<Appliance>> listDataChild;
    private volatile String dlText;
    public volatile House main;
    public volatile Room kitchen;
    public volatile Room livingRoom;
    public volatile Room bedroom;
    public volatile Room bathroom;
    public String url = new String ("http://192.168.1.5:8080/bierz");
    public volatile String json;
    public volatile String lastJson;
    public JSONDecoder jsonDec;
    Resources resources;
    private volatile int lastExpandedPosition = -1;
    Handler h = new Handler();
    final int dlDelay = 6000; //milliseconds
    volatile FileHandler fileHandler = new FileHandler(this);
    JSONObject jsonObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resources = getResources();
        super.onCreate(savedInstanceState);
        dlText = null;
        setContentView(R.layout.activity_wireless_home);

        //fileHandler.writer();


        //TextView determining connection
        final TextView txtStart = (TextView) findViewById(R.id.textView);
        new Downloader(){
            @Override
            protected String doInBackground(String... s){
                return "";
            }

            @Override
            protected void onPostExecute(String result) {
                if (checkConnection()) {
                    dlText = "Connected";
                    // TODO Startup Downloader
                } else {
                    dlText = "No network connection. Using latest known data.";
                }
                setTextView(txtStart, dlText);;

            }
        }.execute(url);


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        //declare rooms
        declareRooms(fileHandler.firstReader());

        // prepare room list
        prepareRoomList();

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        listAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
            }
        });

        // setting list adapter
        expListView.setAdapter(listAdapter);


        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });



        // Thread downloading JSON from Spring Serv every 5 sec
        h.postDelayed(new Runnable() {
            public void run() {
                // TODO Downloader and Main View Refresher's Focus

                // ADD condition to refresh only when new data appear in DB

                runOnUiThread(new Runnable() {
                    public void run() {
                        /*
                        prepareRoomList();
                        json = jsonDec.encodeRooms(roomList);
                        try {
                            jsonObj = new JSONObject(json);
                        }catch (JSONException e){customToast("JSONExc");}
                        fileHandler.writerOnJSON(json);
                        */

                        /***
                         * ZROBIC TAK, ZEBY POBIERALO JSON I PODMIENIALO NA NOWE
                         */

                        new Downloader() {
                            @Override
                            protected String doInBackground(String... s) {
                                return downloadJSON("http://192.168.1.5:8080/get_house");
                            }


                            @Override
                            protected void onPostExecute(String result) {
                                json = result;
                                if (lastJson!=json) {
                                    declareRooms(json);
                                    prepareRoomList();
                                    prepareListData();
                                    //customToast(jsonDec.encodeRooms(roomList));
                                    listAdapter = new ExpandableListAdapter(getBaseContext(), listDataHeader, listDataChild);
                                    expListView.setAdapter(listAdapter);
                                    if (lastExpandedPosition != -1) {
                                        expListView.expandGroup(lastExpandedPosition);
                                        expListView.setSelectedGroup(lastExpandedPosition);
                                    }
                                    lastJson = json;
                                }
                                else{
                                    json = jsonDec.encodeRooms(roomList);
                                    new Downloader() {
                                        @Override
                                        protected String doInBackground(String... s) {
                                            sendJSON("http://192.168.1.5:8080/bierz", json);
                                            return "";
                                        }

                                        @Override
                                        protected void onPostExecute(String result) {
                                        }
                                    }.execute(url);
                                }
                            }
                        }.execute(url);




                        //fileHandler.writerOnJSON(jsonDec.encodeRooms(roomList));
                        //customToast(fileHandler.reader());

                        //declareRooms(jsonDec.encodeRooms(roomList))






                    }
                });


                h.postDelayed(this, dlDelay);
            }
        }, dlDelay);

    }


    /*
    Prepare rooms
     */

    private void declareRooms(String json)
    {
        main = new House("Main Control");
        kitchen = new Room("Kitchen");
        livingRoom = new Room("Living Room");
        bedroom = new Room("Bedroom");
        bathroom = new Room("Bathroom");

        jsonDec = new JSONDecoder();

        //Main control
        main.setAppliances(jsonDec.decodeRoom(json, "main"));

        //Kitchen
        kitchen.setAppliances(jsonDec.decodeRoom(json, "kitchen"));

        //Living Room
        livingRoom.setAppliances(jsonDec.decodeRoom(json, "livingRoom"));

        //Bedroom
        bedroom.setAppliances(jsonDec.decodeRoom(json, "bedroom"));

        //Bathroom
        bathroom.setAppliances(jsonDec.decodeRoom(json, "bathroom"));

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

    private boolean checkConnection() {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
    }

    private void setTextView(TextView v, String s){
        v.setText(s);
    }

    public String getJSON(){
        return jsonDec.encodeRooms(roomList);
    }

    private String fileReader(){
        InputStream is;
        resources = getResources();
        int rID = resources.getIdentifier("henny.wirelesshouse:raw/json", null, null);
        is = resources.openRawResource(rID);
        try {
            byte[] buffer = new byte[is.available()];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            is.read(buffer);
            os.write(buffer);
            os.close();
            is.close();
            return os.toString();
        }catch (IOException e) {
            customToast("IO Exception in buffer");
            return "IO Exception in buffer";
        }
    }



}

