package br.com.moduloteste;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;

import br.com.moduloteste.controller.OrganizationController;
import br.com.moduloteste.objects.Asset;

public class NewAssetActivity extends ActionBarActivity {

    private String address;
    private String token;
    private Asset asset = new Asset();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_asset);

        //Preenchimento das combos
        Spinner assetTypeSpinner = (Spinner) findViewById(R.id.asset_type);
        ArrayAdapter<CharSequence> assetTypeAdapter = ArrayAdapter.createFromResource(this, R.array.asset_type, android.R.layout.simple_spinner_item);
        assetTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetTypeSpinner.setAdapter(assetTypeAdapter);

        Spinner perimeterSpinner = (Spinner) findViewById(R.id.perimeter);
        ArrayAdapter<CharSequence> perimeterAdapter = ArrayAdapter.createFromResource(this, R.array.perimeter, android.R.layout.simple_spinner_item);
        perimeterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        perimeterSpinner.setAdapter(perimeterAdapter);

        Spinner relevanceSpinner = (Spinner) findViewById(R.id.relevance);
        ArrayAdapter<CharSequence> relevanceAdapter = ArrayAdapter.createFromResource(this, R.array.relevance, android.R.layout.simple_spinner_item);
        relevanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relevanceSpinner.setAdapter(relevanceAdapter);
    }

    public void insertAsset(View view){
        try {
            int addressId = R.string.enviroment_url;
            address = (String) getResources().getText(addressId);

            Intent intent = getIntent();
            token = intent.getStringExtra(MainActivity.TOKEN);

            EditText editAssetName = (EditText) findViewById(R.id.asset_name);
            asset.setName(editAssetName.getText().toString());

            Spinner perimeterSpinner = (Spinner) findViewById(R.id.perimeter);
            asset.setPerimeter(perimeterSpinner.getSelectedItem().toString());

            Spinner assetTypeSpinner = (Spinner) findViewById(R.id.asset_type);
            asset.setType(assetTypeSpinner.getSelectedItem().toString());

            Spinner relevanceSpinner = (Spinner) findViewById(R.id.relevance);
            asset.setRelevance(relevanceSpinner.getSelectedItem().toString());

            new process().execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class process extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... params) {
            OrganizationController organization = new OrganizationController();
            String oid = "";
            try {
               oid = organization.newAsset(asset.generateJSON(asset), token, address);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return oid;
        }

        @Override
        protected void onPostExecute(String oid) {
            Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();
        }

    }

}
