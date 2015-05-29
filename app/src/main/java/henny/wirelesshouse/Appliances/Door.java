package henny.wirelesshouse.Appliances;

import android.widget.TextView;

/**
 * Created by Henny on 2015-04-01.
 */
public class Door implements Appliance {
    private String name = "";
    private String state = "";
    private TextView nameView;
    private TextView stateView;

    public Door(){
    }

    public Door(String state, String name){
        this.state = state;
        this.name = name;
    }

    public Door(Door door){
        this.name = door.getName();
        this.state = door.getState();
    }

    public void setNameView(TextView v){
        nameView = v;
    }

    public void setStateView(TextView v){
        stateView = v;
    }

    public void setNameViewTxt(String s){
        nameView.setText(s);
    }

    public void setStateViewTxt(String s){
        stateView.setText(s);
    }


    public void setState(String state){
        this.state = state;
    }

    public String getState(){
       return state;
    }

    public void setName(String name){
        this.name = name;
    }


    public String getName(){ return name; }


}
