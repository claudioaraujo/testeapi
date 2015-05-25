package br.com.moduloteste.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.moduloteste.api.call.HttpsCall;
import br.com.moduloteste.objects.User;

/**
 * Created by claudio.araujo on 21/05/2015.
 */
public class UserController {
    public User getInfo (String address, String token) throws JSONException {
        User user = new User();
        try{
            HttpsCall httpsCall = new HttpsCall();
            JSONObject userJson = new JSONObject(httpsCall.getJson(address, token));

            user.setName(userJson.getString("Name"));
            user.setLogin(userJson.getString("Login"));
            user.setEmail(userJson.getString("Email"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    public User getInfoProfiles (String address, String token, User user) throws JSONException {
        String response = "";
        try{
            HttpsCall httpsCall = new HttpsCall();
            response = httpsCall.getJson(address, token);

            user.setMember_of(new ArrayList<String>());

        }catch (Exception e){
            e.printStackTrace();
        }
        return user.degenerateJson(user, response);
    }
}
