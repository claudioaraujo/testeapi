package br.com.moduloteste;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.com.moduloteste.api.call.HttpsCall;
import br.com.moduloteste.controller.GeoLocationController;


public class MainActivity extends Activity {

    int clientId = R.string.client_id;
    int clientSecret = R.string.client_secret;
    int redirectUri = R.string.redirect_uri;
    int grantType = R.string.grant_type;
    int tokenUrl = R.string.token_url;
    int oauthUrl = R.string.oauth_url;

    private static String CLIENT_ID = "";
    private static String CLIENT_SECRET = "";
    private static String REDIRECT_URI = "";
    private static String GRANT_TYPE = "";
    private static String TOKEN_URL = "";
    private static String OAUTH_URL = "";

    public final static String TOKEN = "";

    //Codigo migrado para strings.xml
    //private static String CLIENT_ID = "7fbece65dc0447f7824c589f1f86e0eb";
    //private static String CLIENT_SECRET ="1504e09e14f14bd5a749652d40652473";
    //private static String REDIRECT_URI="calls://auth";
    //private static String GRANT_TYPE="authorization_code";
    //private static String ENVIROMENT_URL ="https://demo.riskmanager.modulo.com/RM_240";
    //private static String TOKEN_URL ="https://demo.riskmanager.modulo.com/RM_240/APIIntegration/Token";
    //private static String OAUTH_URL ="https://demo.riskmanager.modulo.com/RM_240/APIIntegration/AuthorizeFeatures";
    //private static String CONT_ASSET_URL ="https://demo.riskmanager.modulo.com/RM_240/api/organization/assets/count";

    private Bundle location;
    //Change the Scope as you need
    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;
    private static String tok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Identificação do ambiente
        CLIENT_ID = (String) this.getResources().getText(clientId);
        CLIENT_SECRET = (String) this.getResources().getText(clientSecret);
        REDIRECT_URI = (String) this.getResources().getText(redirectUri);
        GRANT_TYPE = (String) this.getResources().getText(grantType);
        TOKEN_URL = (String) this.getResources().getText(tokenUrl);
        OAUTH_URL = (String) this.getResources().getText(oauthUrl);

        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access =(TextView)findViewById(R.id.Access);
        auth = (Button)findViewById(R.id.auth);

        auth.setOnClickListener(new View.OnClickListener() {
            Dialog auth_dialog;
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                auth_dialog = new Dialog(MainActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                web = (WebView)auth_dialog.findViewById(R.id.webv);
                web.getSettings().setJavaScriptEnabled(true);
                web.loadUrl(OAUTH_URL+"?redirect_uri="+REDIRECT_URI+"&response_type=code&client_id="+CLIENT_ID);
                web.setWebViewClient(new WebViewClient() {

                    boolean authComplete = false;
                    Intent resultIntent = new Intent();

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon){
                        super.onPageStarted(view, url, favicon);

                    }
                    String authCode;
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        if (url.contains("?code=") && authComplete != true) {
                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            Log.i("", "CODE : " + authCode);
                            authComplete = true;
                            resultIntent.putExtra("code", authCode);
                            MainActivity.this.setResult(Activity.RESULT_OK, resultIntent);
                            setResult(Activity.RESULT_CANCELED, resultIntent);

                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("Code", authCode);
                            edit.commit();
                            auth_dialog.dismiss();
                            new TokenGet().execute();
                            Toast.makeText(getApplicationContext(), "Código de acesso: " + authCode, Toast.LENGTH_SHORT).show();
                        }else if(url.contains("error=access_denied")){
                            Log.i("", "ACCESS_DENIED_HERE");
                            resultIntent.putExtra("code", authCode);
                            authComplete = true;
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();

                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.show();
                auth_dialog.setTitle("Login no RM");
                auth_dialog.setCancelable(true);
            }
        });
    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        String Code;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Conectando ao RM ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            Code = pref.getString("Code", "");
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            GetAccessToken jParser = new GetAccessToken();
            HttpsCall httpsCall = new HttpsCall();

            JSONObject json = null;
            JSONObject jsonUser = null;
            try {
                json = jParser.getToken(TOKEN_URL, Code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE);
                if (json != null) {
                    tok = json.getString("access_token");
                    //jsonUser = httpsCall.getUser(tok, ENVIROMENT_URL);
                    //System.out.print("r");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (json != null){
                try {
                    //tok = json.getString("access_token");
                    auth.setText("Autenticado");
                    Access.setText(tok);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
    }

    public void showNewAsset(View view) {
        Intent intent = new Intent(this, NewAssetActivity.class);
        TextView tokenText = (TextView) findViewById(R.id.Access);
        String token = tokenText.getText().toString();
        intent.putExtra(TOKEN, token);
        startActivity(intent);
    }

}
