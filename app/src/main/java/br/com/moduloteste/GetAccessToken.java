package br.com.moduloteste;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by claudio.araujo on 13/05/2015.
 */
public class GetAccessToken {

    private final String USER_AGENT = "Mozilla/5.0";
    public JSONObject getToken(String address,String token,String client_id,String client_secret,String redirect_uri,String grant_type) throws IOException, JSONException {
        // Making HTTP request

        //String urlParameters = "grant_type="+grant_type+"&code="+token+"&client_id="+client_id+"&client_secret="+client_secret+"&redirect_uri="+redirect_uri;
        //String urlString = "https://macalpine.riskmanager.modulo.com/RM_DEMO_RJ/APIIntegration/Token";
        String response = "";

        URL url = new URL(address);
        HttpsURLConnection httpsConnection=(HttpsURLConnection)url.openConnection();

        //Parâmetros da chamada
        Map<String,Object> variables = new LinkedHashMap<>();
        variables.put("grant_type", grant_type);
        variables.put("code", token);
        variables.put("client_id", client_id);
        variables.put("client_secret", client_secret);
        variables.put("redirect_uri", redirect_uri);

        httpsConnection.setDoInput(true);
        httpsConnection.setDoOutput(true);
        httpsConnection.setUseCaches(false);
        httpsConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        String postData="";
        for (  String key : variables.keySet()) {
            postData+="&" + key + "="+ variables.get(key);
        }
        postData=postData.substring(1);
        DataOutputStream postOut=new DataOutputStream(httpsConnection.getOutputStream());
        postOut.writeBytes(postData);
        postOut.flush();
        postOut.close();
        int responseCode=httpsConnection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        JSONObject json = new JSONObject(response);

        return json;

    }

}
