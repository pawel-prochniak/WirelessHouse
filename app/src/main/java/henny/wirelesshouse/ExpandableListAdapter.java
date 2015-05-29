package henny.wirelesshouse;

/**
 * Created by Henny on 2015-04-01.
 * Room list adapter
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import henny.wirelesshouse.Appliances.Appliance;
import henny.wirelesshouse.Appliances.Door;
import henny.wirelesshouse.Appliances.Heating;
import henny.wirelesshouse.Appliances.Light;
import henny.wirelesshouse.Appliances.LightBlind;
import henny.wirelesshouse.Appliances.Temperature;
import henny.wirelesshouse.Appliances.Window;
import henny.wirelesshouse.Appliances.WindowBlind;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Appliance>> _listDataChild;
    URL url;
    URLConnection urlCon;
    DataInputStream inputStream;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Appliance>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    JSONDecoder jsonDec;


    @Override
    public Appliance getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        Appliance child;
        child = getChild(groupPosition, childPosition);
        final String childText = child.getName();

        if (child instanceof Door) {
            Door door;
            door = (Door) getChild(groupPosition, childPosition);

                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.door, null);


            //Setting name of the door
            door.setNameView((TextView) convertView
                    .findViewById(R.id.doorText));
            door.setNameViewTxt(childText);
            //Setting checkbox status
            door.setStateView((TextView) convertView
                    .findViewById(R.id.doorOpen));
            door.setStateViewTxt(door.getState());
            return convertView;
        }

        else if (child instanceof LightBlind){
            final LightBlind light;
            light = (LightBlind) getChild(groupPosition, childPosition);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.light_blind, null);

            //Setting light name
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lightText);
            txtListChild.setText(childText);

            //Setting light status
            final TextView txtLightState = (TextView) convertView
                    .findViewById(R.id.lightState);
            txtLightState.setText(child.getState());

            //Setting checkbox
            CheckBox lightCheckBox= (CheckBox) convertView
                     .findViewById(R.id.lightCheckBox);
            lightCheckBox.setChecked(light.isOn());

            //Setting light bar
            final SeekBar lightBlindBar= (SeekBar) convertView
                    .findViewById(R.id.light_bar);
            lightBlindBar.setProgress(light.getBlind());

            //Setting light checkbox listener
            lightCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    light.switchLight();
                    txtLightState.setText(light.getState());
                    lightBlindBar.setEnabled(light.isOn());
                }
            });

            //Setting light bar listener
            lightBlindBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    light.setBlind(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar){

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(_context, "Light set to: "+String.valueOf(light.getBlind())+"%",Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        else if (child instanceof Light){
            final Light light;
            light = (Light) getChild(groupPosition, childPosition);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.light, null);

            //Setting light name
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lightText);
            txtListChild.setText(childText);
            //Setting light status
            final TextView txtLightState = (TextView) convertView
                    .findViewById(R.id.lightState);
            txtLightState.setText(child.getState());
            //Setting light checkbox
            final CheckBox lightCheckBox= (CheckBox) convertView
                    .findViewById(R.id.lightCheckBox);
            lightCheckBox.setChecked(light.isOn());

            //Setting light checkbox listener
            lightCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    light.switchLight();
                    txtLightState.setText(light.getState());
                }
            });

            return convertView;
        }

        else if (child instanceof WindowBlind){
            final WindowBlind window;
            window = (WindowBlind) getChild(groupPosition, childPosition);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.window_blind, null);

            //Setting window name
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.windowText);
            if(window.getBlind()==0) { txtListChild.setText(childText); }
            else { txtListChild.setText(childText); }

            //Setting spinner
            Spinner windowSpinner = (Spinner) convertView
                    .findViewById(R.id.window_spinner);
            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this._context,
                    R.array.window_spinner_choices, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            windowSpinner.setAdapter(adapter);
            windowSpinner.setSelection(adapter.getPosition(window.getState()));
            windowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    window.setState(position);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }


            });


            //Setting window bar
            SeekBar windowBlindBar= (SeekBar) convertView
                    .findViewById(R.id.window_bar);
            windowBlindBar.setProgress(window.getBlind());

            //Setting window bar listener
            windowBlindBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    window.setBlind(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(_context, "Rollers unrolled: "+window.getBlind()+"%",Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }

        else if (child instanceof Window) {
            final Window window;
            window = (Window) getChild(groupPosition, childPosition);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.window, null);

            //Setting name of the window
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.windowText);
            txtListChild.setText(childText);

            //Setting spinner
            Spinner windowSpinner = (Spinner) convertView
                    .findViewById(R.id.window_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this._context,
                    R.array.window_spinner_choices, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            windowSpinner.setAdapter(adapter);
            windowSpinner.setSelection(adapter.getPosition(window.getState()));
            windowSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    window.setState(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }


            });

            return convertView;
        }

        if (child instanceof Heating) {
            final Heating heating;
            heating = (Heating) getChild(groupPosition, childPosition);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.heating, null);

            //Setting name of the door
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.heatingText);
            txtListChild.setText(childText);
            //Setting temperature
            TextView txtTemperature = (TextView) convertView
                    .findViewById(R.id.temperature);
            txtTemperature.setText(heating.getState());
            //Setting set temperature
            final TextView txtNewTemperature = (TextView) convertView
                    .findViewById(R.id.temperature_set);
            txtNewTemperature.setText(heating.getNewTemperature());
            //Setting bar status
            SeekBar seekBar = (SeekBar) convertView
                    .findViewById(R.id.temperature_bar);
            seekBar.setProgress(heating.getTemperature()/2+10);

            //Setting window bar listener
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    heating.setNewTemperature(progress/2+10);
                    txtNewTemperature.setText(heating.getNewTemperature());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    Toast.makeText(_context, "Temperature set to: "+heating.getNewTemperature(),Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;
        }

        if (child instanceof Temperature) {
            Temperature temperature;
            temperature = (Temperature) getChild(groupPosition, childPosition);

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.temperature, null);

            //Setting name of the door
            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.heatingText);
            txtListChild.setText(childText);
            //Setting temperature outside
            TextView txtDoorOpen = (TextView) convertView
                    .findViewById(R.id.temperature);
            txtDoorOpen.setText(temperature.getState());
            return convertView;
        }

        else{

                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);


            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
            }

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}