package club.ubly.ubly.Network;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.neno0o.ubersdk.Auth.Models.AccessToken;
import com.neno0o.ubersdk.Uber;
import com.neno0o.ubersdk.UberURLs;

import javax.inject.Inject;

import club.ubly.ubly.UblyApplication;
import club.ubly.ubly.Util.BackgroundLooper;
import club.ubly.ubly.Util.LyftDao;
import club.ubly.ubly.Util.MainLooper;
import club.ubly.ubly.Util.UserTokenResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Scheduler;
import rx.functions.Action1;

/**
 * Created by jessesum on 2/28/16.
 */
public class LyftAuthentication extends AppCompatActivity {

    // Getting intent from the application
    Intent intent;
    @Inject
    public LyftAuthService mLystService;
    @Inject
    @BackgroundLooper
    Scheduler mBackgroundScheduler;
    @Inject
    @MainLooper
    Scheduler mMainScheduler;
    @Inject
    LyftDao mLyftDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(com.neno0o.ubersdk.R.layout.authentication);

        ((UblyApplication)getApplication()).applicationComponent().inject(this);

        WebView webView = (WebView) findViewById(com.neno0o.ubersdk.R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                LyftAuthentication.this.setProgress(progress * 1000);
            }
        });

        webView.setWebViewClient(new UberWebViewClient());

        webView.loadUrl(buildUrl());

        intent = new Intent();
    }


    private String buildUrl() {
        Uri.Builder uriBuilder = Uri.parse("https://api.lyft.com/oauth/authorize").buildUpon();
        uriBuilder.appendQueryParameter("response_type", "code");
        uriBuilder.appendQueryParameter("client_id", "D35DoULU-GiX");
        uriBuilder.appendQueryParameter("scope", "public");
        uriBuilder.appendQueryParameter("state", "");
        return uriBuilder.build().toString().replace("%20", "+");
    }

    private class UberWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return checkRedirect(url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d("jesse", "This is on error");
            if (checkRedirect(failingUrl)) {
                return;
            }
            Toast.makeText(LyftAuthentication.this, "Error " + description, Toast.LENGTH_SHORT).show();
        }

        private boolean checkRedirect(String url) {
            Log.d("jesse", "this is the redirect");
            if (url.startsWith("https://twitter.com/jhysum")) {
                Uri uri = Uri.parse(url);

                mLystService.getUserToken("authorization_code",uri.getQueryParameter("code"))
                        .subscribeOn(mBackgroundScheduler)
                        .observeOn(mMainScheduler)
                        .subscribe(userTokenResponse -> {

                            Log.d("jesse", "this is the user token: " + userTokenResponse.getAccessToken());
                            mLyftDao.setUserToken(userTokenResponse.getAccessToken());
                            Log.d("jesse", uri.getQueryParameter("code"));
                            setResult(RESULT_OK, intent);
                            finish();
                        }, throwable -> {

                        });

//                Uber.getInstance().getUberAuthService().getAuthToken(
//                        Uber.getInstance().getClientSecret(),
//                        Uber.getInstance().getClientId(),
//                        Uber.getInstance().getGrantType(),
//                        uri.getQueryParameter("code"),
//                        Uber.getInstance().getClientRedirectUri(),
//                        new Callback<AccessToken>() {
//                            @Override
//                            public void success(AccessToken accessToken, Response response) {
//                                Uber.getInstance().setAccessToken(accessToken);
//                                setResult(RESULT_OK, intent);
//                                finish();
//                            }
//
//                            @Override
//                            public void failure(RetrofitError error) {
//
//                            }
//                        }
//                );
                return true;
            }
            return false;
        }
    }
}


