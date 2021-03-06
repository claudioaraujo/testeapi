package br.com.moduloteste.api.call;

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
 * Created by claudio.araujo on 18/05/2015.
 */
public class HttpsCall {
    private final String USER_AGENT = "Mozilla/5.0";

    public String sendPostNewAsset (String token, String address, String json) throws IOException {
        String oid = "";

        URL url = new URL(address);
        HttpsURLConnection httpsConnection = (HttpsURLConnection)url.openConnection();

        httpsConnection.setDoInput(true);
        httpsConnection.setDoOutput(true);
        httpsConnection.setUseCaches(false);
        httpsConnection.setRequestProperty("Content-Type", "application/json");
        httpsConnection.setRequestProperty("Authorization", "OAuth2 " + token);

        DataOutputStream postOut=new DataOutputStream(httpsConnection.getOutputStream());
        postOut.writeBytes(json);
        postOut.flush();
        postOut.close();
        int responseCode=httpsConnection.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_CREATED) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
            while ((line=br.readLine()) != null) {
                oid += line;
            }
        }

        return oid;
    }
}
