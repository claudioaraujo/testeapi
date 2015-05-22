package br.com.moduloteste.controller;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.moduloteste.api.call.HttpsCall;
import br.com.moduloteste.objects.User;

/**
 * Created by claudio.araujo on 21/05/2015.
 */
public class UserController {
    public User getInfo (String address, String token) throws JSONException {
        User user = new User();
        JSONObject userJson = null;

        HttpsCall httpsCall = new HttpsCall();
        userJson = httpsCall.getJson(address, token);

        user.setName(userJson.getString("Name"));
        user.setLogin(userJson.getString("Login"));
        user.setEmail(userJson.getString("Email"));

        return user;
    }
}
