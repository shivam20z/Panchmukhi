package com.panchmukhi.eclinic;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static SessionManager jInstance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private SessionManager(Context context) {
        prefs = context.getSharedPreferences("Your_Preference_name", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (jInstance != null) {
            return jInstance;
        } else {
            jInstance = new SessionManager(context);
            return jInstance;
        }
    }






    public void setIsLogin(Boolean state){
        editor.putBoolean("login",state);
        editor.apply();
    }

    public boolean getIsLogin() {


        return prefs.getBoolean("login", false); // you can set default size as you want
    }

    public void setPickUpMapLocation(String state){
        editor.putString("PickUpMapLocation",state);
        editor.apply();
    }

    public String getPickUpMapLocation(){
        return prefs.getString("PickUpMapLocation","");
    }


    public void setPickUpName(String state){
        editor.putString("PickUpName",state);
        editor.apply();
    }

    public String getPickUpName(){
        return prefs.getString("PickUpName","");
    }


    public void setPickUpNumber(String state){
        editor.putString("PickUpNumber",state);
        editor.apply();
    }

    public String getPickUpNumber(){
        return prefs.getString("PickUpNumber","");
    }


    public void setPickUpFlatNo(String state){
        editor.putString("PickUpFlatNo",state);
        editor.apply();
    }

    public String getPickUpFlatNo(){
        return prefs.getString("PickUpFlatNo","");
    }


    public void setPickUpLandMark(String state){
        editor.putString("PickUpLandMark",state);
        editor.apply();
    }

    public String getPickUpLandMark(){
        return prefs.getString("PickUpLandMark","");
    }


    public void setPickUpLatLng(String state){
        editor.putString("PickUpLatLng",state);
        editor.apply();
    }

    public String getPickUpLatLng(){
        return prefs.getString("PickUpLatLng","");
    }

    public void setPickUpEmail(String state){
        editor.putString("PickUpEmail",state);
        editor.apply();
    }

    public String getPickUpEmail(){
        return prefs.getString("PickUpEmail","");
    }


    public void setShopUid(String state){
        editor.putString("ShopUid",state);
        editor.apply();
    }

    public String getShopUid(){
        return prefs.getString("ShopUid","");
    }

    public void setAppointmentDate(String state){
        editor.putString("AppointmentDate",state);
        editor.apply();
    }

    public String getAppointmentDate(){
        return prefs.getString("AppointmentDate","");
    }


    public void setAppointmentDay(String state){
        editor.putString("AppointmentDay",state);
        editor.apply();
    }

    public String getAppointmentDay(){
        return prefs.getString("AppointmentDay","");
    }

    public void setAppointmentTime(String state){
        editor.putString("AppointmentTime",state);
        editor.apply();
    }

    public String getAppointmentTime(){
        return prefs.getString("AppointmentTime","");
    }

    public void setShopName(String state){
        editor.putString("ShopName",state);
        editor.apply();
    }

    public String getShopName(){
        return prefs.getString("ShopName","");
    }



    public Boolean setRemove_DONOTSHOWMEAGAIN(Boolean state){
        editor.putBoolean("remove",state);
        editor.apply();
        return state;
    }

    public boolean getRemove_DONOTSHOWMEAGAIN() {


        return prefs.getBoolean("remove", false); // you can set default size as you want
    }

    public void clearPreference(){
        prefs.edit().remove("PickUpMapLocation").apply();
        prefs.edit().remove("PickUpName").apply();
        prefs.edit().remove("PickUpNumber").apply();
        prefs.edit().remove("PickUpFlatNo").apply();
        prefs.edit().remove("PickUpLandMark").apply();
        prefs.edit().remove("PickUpLatLng").apply();
        prefs.edit().remove("PickUpEmail").apply();
        prefs.edit().remove("ShopUid").apply();
        prefs.edit().remove("AppointmentDate").apply();
        prefs.edit().remove("AppointmentDay").apply();
        prefs.edit().remove("AppointmentTime").apply();
        prefs.edit().remove("ShopName").apply();
    }
}