package br.com.moduloteste.controller;

import java.io.IOException;

import br.com.moduloteste.api.call.HttpsCall;

/**
 * Created by claudio.araujo on 15/05/2015.
 */
public class OrganizationController {

    public String newAsset(String assetJSON, String token, String address) throws IOException {
        try {
            HttpsCall apiCall = new HttpsCall();
            address = address + "/api/Organization/assets";

            String oid = apiCall.sendPostNewAsset(token, address, assetJSON);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return assetJSON;
    }
}
