package in.digitechlab.restaurantlocator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by DELL on 10/22/2017.
 */

public class RestMapActivity extends AppCompatActivity {

    private Boolean isBackPressed=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_map);

        String mapLink = null;
        if (getIntent().hasExtra("map_link")) {
            mapLink = getIntent().getStringExtra("map_link");
            Toast.makeText(getApplicationContext(),"Connecting to Google Map . . .", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"Please Wait . . .", Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),"MAP is loading . . .", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Map Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        WebView webview = (WebView) findViewById(R.id.webView1);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(mapLink);


/*    private WebView mWebview=null ;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rest_map);

        if (getIntent().hasExtra("map_link")) {
            String mapLink = getIntent().getStringExtra("map_link");
            Toast.makeText(getApplicationContext(), mapLink, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "No Map Found", Toast.LENGTH_SHORT).show();
            finish();
        }

        mWebview = (WebView) findViewById(R.id.webView1);

        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        mWebview.loadUrl();
        setContentView(mWebview );

    }*/

    }

}
