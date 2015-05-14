package br.com.moduloteste;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by claudio.araujo on 14/05/2015.
 */
public class GetTotalAsset {
    private final String USER_AGENT = "Mozilla/5.0";

    public int contAsset(String token, String address) throws IOException {
        address = address+"?access_token="+token;

        URL url = new URL(address);
        HttpsURLConnection httpsConnection=(HttpsURLConnection)url.openConnection();

        httpsConnection.setRequestMethod("GET");
        httpsConnection.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = httpsConnection.getResponseCode();

        return  responseCode;
    }

}
