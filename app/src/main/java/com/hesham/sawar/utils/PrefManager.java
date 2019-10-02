package com.hesham.sawar.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hesham.sawar.data.model.UserPojo;

import java.lang.reflect.Type;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences constants
    private static final String PREF_NAME = "MyPreference";


    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public int getCenterId() {
        return pref.getInt("centerId", 0);
    }

    public void setCenterId(int centerId) {
        editor.putInt("centerId", centerId);
        editor.commit();
    }

    public int getSubjectId() {
        return pref.getInt("SubjectId", 0);
    }

    public void setSubjectId(int SubjectId) {
        editor.putInt("SubjectId", SubjectId);
        editor.commit();
    }
    public int getUniversityId() {
        return pref.getInt("niversityId", 0);
    }

    public void setUniversityId(int niversityId) {
        editor.putInt("niversityId", niversityId);
        editor.commit();
    }

    public int getFacultyId() {
        return pref.getInt("FacultyId", 0);
    }

    public void setFacultyId(int FacultyId) {
        editor.putInt("FacultyId", FacultyId);
        editor.commit();
    }

    public int getType() {
        return pref.getInt("type", 0);
    }

    public void setType(int type) {
        editor.putInt("type", type);
        editor.commit();
    }
    public String getToken() {
        return pref.getString("Token", "");
    }

    public void setToken(String token) {
        editor.putString("Token", token);
        editor.commit();
    }
    public String getImageProfile() {
        return pref.getString("ImageProfile", "");
    }

    public void setImageProfile(String ImageProfile) {
        editor.putString("ImageProfile", ImageProfile);
        editor.commit();
    }
//    public String getPasswordToken() {
//        return pref.getString("passwordToken", "");
//    }
//
//    public void setPasswordToken(String passwordToken) {
//        editor.putString("passwordToken", passwordToken);
//        editor.commit();
//    }


    public UserPojo getCenterData() {
        Gson gson = new Gson();
        String json = pref.getString("centerData", "");
        UserPojo centerData = gson.fromJson(json, UserPojo.class);
        return centerData;
    }

    public void setCenterData(UserPojo centerData) {
        Gson gson = new Gson();
        String json = gson.toJson(centerData);
        editor.putString("centerData", json);
        editor.commit();
    }
    public UserPojo getUserPojo() {
        Gson gson = new Gson();
        String json = pref.getString("UserPojo", "");
        UserPojo association = gson.fromJson(json, UserPojo.class);
        return association;
    }

    public void setUserPojo(UserPojo association) {
        Gson gson = new Gson();
        String json = gson.toJson(association);
        editor.putString("UserPojo", json);
        editor.commit();
    }


    public void removeAll() {
        editor.remove("UserPojo");
        editor.remove("centerData");
        editor.apply();
    }


//    public String getCreateAssociationPhoto() {
//        return pref.getString("CreateAssociationPhoto", "");
//    }
//    public void setCreateAssociationPhoto(String CreateAssociationPhoto) {
//        editor.putString("CreateAssociationPhoto", CreateAssociationPhoto);
//        editor.commit();
//    }



//    public ArrayList<Association> getAssociations() {
//        Gson gson = new Gson();
//        String json = pref.getString("Association", "");
//        Type type = new TypeToken<ArrayList<Association>>() {}.getType();
//        return gson.fromJson(json, type);
//    }

}
