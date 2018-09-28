package com.extra.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shayan on 6/30/2017.
 * this is a class to save user info and use then when needed
 */

public class AccountManager {

    private static AccountManager accountManager;
    private static String NAME = "user";

    /**
     * account manager singleton
     *
     * @param context using to get shared preferences
     * @return AccountManager
     */
    public static AccountManager getInstanse(Context context) {
        if (accountManager == null) {
            accountManager = new AccountManager(context);
        }
        return accountManager;
    }

    /**
     * context to access the shared preferences
     */
    private Context context;
    /**
     * name of the shared preferences file
     */
    private String className = getClass().getName();
    /**
     * key set of the saved data
     */
    private String isLogin = "islogin";
    private String password = "password";
    private String token = "token";
    private String name = "name";
    private String lastName = "family";
    private String email = "email";
    private String phoneNumber = "mobile";
    private String photo = "profile_pic";
    private String driverSmartCard = "driverSmartCard";
    private String carSmartCart = "carSmartCard";
    private String nationalCode = "nationalCode";
    private String carYear = "carYear";
    private String carType = "carType";
    private String tedadMehvar = "Tedad_mehvar";
    private String father = "father";
    private String birthDay = "birthday";
    private String pelak = "pelak";
    private String salamat = "salamat_date";


    private SharedPreferences read;
    private SharedPreferences write;

    /**
     * private constructor
     *
     * @param context for access to shared preferences
     */
    private AccountManager(Context context) {
        this.context = context;
        read = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        write = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    /**
     * checks user login state
     *
     * @return true if user is login
     */
    public boolean isLogin() {
        return read.getBoolean(isLogin, false);
    }

    /**
     * get authentication token
     *
     * @return token
     */
    public String getToken() {

        return "Token " + read.getString(token, "");
    }

    public void setToken(String token) {
        write().putString(this.token, token).apply();
    }

    private SharedPreferences.Editor write() {
        return write.edit();
    }

    public void setName(String name) {
        write().putString(this.name, name).apply();
    }

    public void setLastName(String family) {
        write().putString(lastName, family).apply();
    }

    public void setPhoneNumber(String phone_number) {
        write().putString(phoneNumber, phone_number).apply();
    }

    public void setEmail(String email) {
        write().putString(this.email, email).apply();
    }


    public String getName() {
        return read.getString(name, "");
    }

    public String getLastName() {
        return read.getString(lastName, "");
    }

    public String getEmail() {
        return read.getString(email, "");
    }


    public String getPhoneNumber() {
        return read.getString(phoneNumber, "");
    }


    public void setLogin(boolean b) {
        write().putBoolean(isLogin, b).apply();
    }

    public int getId() {
        return read.getInt("id", 0);
    }

    public void setId(int id) {
        write().putInt("id", id).apply();
    }


    public void save(JSONObject object) throws JSONException {
        setPassword(object.getString(password));
        setEmail(object.getString(email));
        setPhoneNumber(object.getString(phoneNumber));
        setName(object.getString(name));
        setLastName(object.getString(lastName));
        setDriverSmart(object.getString(driverSmartCard));
        setCarSmart(object.getString(carSmartCart));
        setNationalCode(object.getString(nationalCode));
        setCarYear(object.getString(carYear));
        setTedadMehvar(object.getString(tedadMehvar));
        setFather(object.getString(father));
        setBirthday(object.getString(birthDay));
        setPelak(object.getString(pelak));
        setSalamat(object.getString(salamat));
        setLogin(true);
    }


    private void setSalamat(String string) {
        write().putString(salamat, string).apply();
    }

    private void setPelak(String string) {
        write().putString(pelak, string).apply();
    }

    private void setBirthday(String string) {
        write().putString(birthDay, string).apply();
    }

    private void setTedadMehvar(String string) {
        write().putString(tedadMehvar, string).apply();
    }

    private void setCarYear(String string) {
        write().putString(carYear, string).apply();
    }

    private void setNationalCode(String string) {
        write().putString(nationalCode, string).apply();
    }

    private void setCarSmart(String string) {
        write().putString(carSmartCart, string).apply();
    }

    private void setDriverSmart(String string) {
        write().putString(driverSmartCard, string).apply();
        write().putString(photo, String.format("http://smartcard.rmto.ir:7003/tto/webservice/get_img/%s,PICTURE,PICTURE123", string)).apply();
        Log.d(getClass().getSimpleName(), "setDriverSmart: " + getPhoto());
    }

    public void setPassword(String password) {
        write().putString(this.password, password).apply();
    }

    public void setFather(String father) {
        write().putString(this.father, father).apply();
    }

    public String getCarSmartCart() {
        return read.getString(carSmartCart, "");
    }

    public String getCarYear() {
        return read.getString(carYear, "");
    }

    public String getDriverSmartCard() {
        return read.getString(driverSmartCard, "");
    }

    public String getBirthDay() {
        return read.getString(birthDay, "");
    }

    public String getFather() {
        return read.getString(father, "");
    }

    public String getNationalCode() {
        return read.getString(nationalCode, "");
    }

    public String getPassword() {
        return read.getString(password, "");
    }

    public String getPelak() {
        return read.getString(pelak, "");
    }

    public String getPhoto() {
        return read.getString(photo, "");
    }

    public String getSalamat() {
        return read.getString(salamat, "");
    }

    public String getTedadMehvar() {
        return read.getString(tedadMehvar, "");
    }

    public void setInfoSet(boolean b) {
        write().putBoolean("info", b).apply();
    }

    public boolean isInfoSet() {
        return read.getBoolean("info", false);
    }

    public boolean isPolicyAccepted() {
        return read.getBoolean("policy", false);
    }

    public void setPolicyAccepted(boolean b) {
        write().putBoolean("policy", b).apply();
    }

    public void setCarSmartCart(String carSmartCart) {
        write().putString(this.carSmartCart, carSmartCart).apply();
    }

    public void setCarType(String carType) {
        write().putString(this.carType, carType).apply();
    }

    public String getCarType() {
        return read.getString(carType, "");
    }

    public void logout() {
        write().clear().apply();
    }
}
