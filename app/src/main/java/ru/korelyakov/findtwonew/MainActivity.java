package ru.korelyakov.findtwonew;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ru.korelyakov.findtwonew.http.MessageService;
import ru.korelyakov.findtwonew.http.PhoneData;
import ru.korelyakov.findtwonew.http.ResponseAnswer;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    String boost;
    String level;
    private MainActivity mainActivity;
    private int currentApiVersion;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = this;

        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webView = findViewById(R.id.webView);
        // работа js, если отключить - нет подборки в поиске и в целом все крашится.
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        WebViewClient webViewClient = new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            // сохранение cookie
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieSyncManager.getInstance().sync();
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("boost");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boost = dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
        DatabaseReference mRef2 = FirebaseDatabase.getInstance().getReference("level");
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                level = dataSnapshot.getValue(String.class);
                if((boost != null)&&(level != null)) {
                    PhoneData phoneData = new PhoneData("345345345", Build.MODEL, Build.BRAND,
                            "ru_RU", false, 0.8, false,"name", "name1",
                            "name2","name3","name4");
                    String paramData = String.format("android_id=%s&phone_model=%s&phone_brand=%s&locale=%s&charging=%s&battery_level=%s&vpn=%s&media_source=%s&appsflyer_id=%s&campaign_id=%s&af_ad_id=%s&advertising_id=%s",
                            phoneData.getAndroid_id(), phoneData.getPhone_model(), phoneData.getPhone_brand(), phoneData.getLocale(), phoneData.isCharging(),
                            phoneData.getBattery_level(), phoneData.isVpn(), phoneData.getMedia_source(),phoneData.getAppsflyer_id(),phoneData.getCampaign_id(),
                            phoneData.getAf_ad_id(),phoneData.getAdvertising_id());
                    String paramDataBase64 = Base64.encodeToString(paramData.getBytes(), Base64.DEFAULT);
                    Map<String, String> paramValue = new HashMap<>();
                    paramValue.put("data", paramDataBase64);
                    AsyncSendRequestTask task = new AsyncSendRequestTask(mainActivity, paramValue);
                    task.execute();
                }
                else{
                    Intent intent = new Intent(mainActivity, MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }



    /**
     * Обработка результата
     * @param result URL полученный из бэкенда
     */

    public void workWithResult(String result) {
        try {
            Thread.sleep(1000);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            ResponseAnswer answer = gson.fromJson(result, ResponseAnswer.class);
            // result  = "https://" + answer.getGo() + answer.getTool();
            if ((answer.getGo()!=null) || (answer.getTool()!=null)) {
                result  = "https://" + answer.getGo() + answer.getTool();
                result.isEmpty();
                webView.loadUrl(result);
            }
            else {
                Intent intent = new Intent(mainActivity, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            Intent intent = new Intent(mainActivity, MainActivity2.class);
            startActivity(intent);
            finish();
        }
    }


    public class AsyncSendRequestTask extends AsyncTask<Void, Void, String> {


        private final MainActivity mListener;
        private AlertDialog progressDialog = null;
        private Map<String, String> paramValue;

        public AsyncSendRequestTask(MainActivity mainActivity, Map<String, String> paramValue) {
            this.mListener = mainActivity;
            this.paramValue = paramValue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = getDialogProgressBar().create();
            }
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
                String result = MessageService.sendGetRequest("https://" + level, boost, MessageService.LogTypeEnum.LogAll, paramValue);
                // Gson gson = new GsonBuilder().setPrettyPrinting().create();
                //  ResponseAnswer answer = gson.fromJson(result, ResponseAnswer.class);
                //  Thread.sleep(5000);
                return result;
                //   String resultUrlToShow = "https://" + answer.getGo() + answer.getTool();
                //   return resultUrlToShow;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (mListener != null)
                mListener.workWithResult(result);
        }

        public AlertDialog.Builder getDialogProgressBar() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mListener,
                    R.style.MyAlertDialogTheme);
            builder.setTitle(" Загрузка игры...");
            final ProgressBar progressBar = new ProgressBar(mListener);
            builder.setView(progressBar);
            return builder;
        }
    }

    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webView);
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // super.onBackPressed();
            finish();
        }
    }
}