package br.com.moduloteste;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends Activity {

    private static String CLIENT_ID = "474428758805435f83472e50987a314b";
    //Use your own client id
    private static String CLIENT_SECRET ="3e67cc9b52d642e397d2296a7c6f5f6a";
    //Use your own client secret
    private static String REDIRECT_URI="calls://auth";
    private static String GRANT_TYPE="authorization_code";
    private static String TOKEN_URL ="https://demo.riskmanager.modulo.com/RM_002/APIIntegration/Token";
    private static String OAUTH_URL ="https://demo.riskmanager.modulo.com/RM_002/APIIntegration/AuthorizeFeatures";
    private static String CONT_ASSET_URL ="https://demo.riskmanager.modulo.com/RM_002/api/organization/assets/count";
    //Change the Scope as you need
    WebView web;
    Button auth;
    SharedPreferences pref;
    TextView Access;
    String tok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("AppPref", MODE_PRIVATE);
        Access =(TextView)findViewById(R.id.Access);
        auth = (Button)findViewById(R.id.auth);
        Button contAsset = (Button) findViewById(R.id.contAsset);
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

        contAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTotalAsset contAsset = new GetTotalAsset();

                try {
                    contAsset.contAsset(tok, CONT_ASSET_URL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            JSONObject json = null;
            try {
                json = jParser.getToken(TOKEN_URL, Code, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            if (json != null){

                try {

                    tok = json.getString("access_token");
                    String expire = json.getString("expires_in");
                    String refresh = json.getString("refresh_token");

                    Log.d("Token Access", tok);
                    Log.d("Expire", expire);
                    Log.d("Refresh", refresh);
                    auth.setText("Autenticado");
                    Access.setText("Access Token:"+tok+"nExpires:"+expire+"nRefresh Token:"+refresh);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }
    }
}
