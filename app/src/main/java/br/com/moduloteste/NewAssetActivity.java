package br.com.moduloteste;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import br.com.moduloteste.controller.GeoLocationController;
import br.com.moduloteste.controller.OrganizationController;
import br.com.moduloteste.objects.Asset;

public class NewAssetActivity extends Activity {

    static final int GET_LOCATION_RESPONSE = 11;
    private String address;
    private String token;
    private Asset asset = new Asset();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_asset);

        token = this.getIntent().getExtras().getString(AssetMainActivity.TOKEN);

        //Preenchimento das combos
        Spinner assetTypeSpinner = (Spinner) findViewById(R.id.asset_type);
        ArrayAdapter<CharSequence> assetTypeAdapter = ArrayAdapter.createFromResource(this, R.array.asset_type, android.R.layout.simple_spinner_item);
        assetTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assetTypeSpinner.setAdapter(assetTypeAdapter);

        TextView perimeterText = (TextView) findViewById(R.id.perimeter);
        perimeterText.setText(this.getIntent().getExtras().getString(AssetMainActivity.PERIMETER));

        Spinner relevanceSpinner = (Spinner) findViewById(R.id.relevance);
        ArrayAdapter<CharSequence> relevanceAdapter = ArrayAdapter.createFromResource(this, R.array.relevance, android.R.layout.simple_spinner_item);
        relevanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        relevanceSpinner.setAdapter(relevanceAdapter);
    }

    public void goGetLocation(View view){
        Intent intent = new Intent(this, GeoLocationController.class);
        startActivityForResult(intent, GET_LOCATION_RESPONSE);
    }

    public void scanBtn(View view){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void insertAsset(View view){
        try {
            int addressId = R.string.enviroment_url;
            address = (String) getResources().getText(addressId);

            EditText editAssetName = (EditText) findViewById(R.id.asset_name);
            asset.setName(editAssetName.getText().toString());

            TextView perimeterText = (TextView) findViewById(R.id.perimeter);
            asset.setPerimeter(perimeterText.getText().toString());

            Spinner assetTypeSpinner = (Spinner) findViewById(R.id.asset_type);
            asset.setType(assetTypeSpinner.getSelectedItem().toString());

            Spinner relevanceSpinner = (Spinner) findViewById(R.id.relevance);
            asset.setRelevance(relevanceSpinner.getSelectedItem().toString());

            EditText editLatitude = (EditText) findViewById(R.id.asset_latitude);
            asset.setLatitude(editLatitude.getText().toString());

            EditText editLongitude = (EditText) findViewById(R.id.asset_longitude);
            asset.setLongitude(editLongitude.getText().toString());

            EditText editDescription = (EditText) findViewById(R.id.asset_description);
            asset.setDescription(editDescription.getText().toString());

            new process().execute();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @TargetApi(21)

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 11){
            super.onActivityResult(requestCode, resultCode, data);
            EditText mLatitudeText = (EditText) findViewById(R.id.asset_latitude);
            EditText mLongitudeText = (EditText) findViewById(R.id.asset_longitude);

            mLatitudeText.setText(String.valueOf(data.getDoubleExtra("Latitude",0)));
            mLongitudeText.setText(String.valueOf(data.getDoubleExtra("Longitude", 0)));
        }else {
            TextView descriptionText = (TextView)findViewById(R.id.asset_description);

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            if (scanContent != null ) {
                descriptionText.setText("Formato: " + scanFormat + " / Codigo: " + scanContent);
            }
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
            if (oid != "") {
                Toast.makeText(getApplicationContext(), "Cadastro realizado com sucesso.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Erro ao tentar cadastrar", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
