package club.ubly.ubly;

import android.app.Application;
import android.util.Log;

import com.neno0o.ubersdk.Uber;

import javax.inject.Inject;

import club.ubly.ubly.Network.LyftAuthService;
import club.ubly.ubly.Network.NetworkModule;
import club.ubly.ubly.Util.BackgroundLooper;
import club.ubly.ubly.Util.LyftDao;
import club.ubly.ubly.Util.MainLooper;
import io.flic.lib.FlicManager;
import rx.Scheduler;

/**
 * Created by jessesum on 2/27/16.
 */
public class UblyApplication extends Application {
    private AppComponent mUblyApp;

    @Inject
    LyftAuthService mLyftApiService;
    @Inject
    Uber mUber;
    @Inject
    LyftDao mLyftDao;
    @Inject
    @BackgroundLooper
    Scheduler mBackgroundScheduler;
    @Inject
    @MainLooper
    Scheduler mMainScheduler;

    @Override
    public void onCreate() {
        super.onCreate();

        mUblyApp = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule()).build();

        mUblyApp.inject(this);

        mLyftApiService.getAccessToken("client_credentials", "public")
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler)
                .subscribe(accessTokenResponse -> {
                    mLyftDao.setAccessToken(accessTokenResponse.getAccessToken());
                }, error -> {
                    Log.d("jesse", "this is the error: " + error);
                });

        mUber.init("0FLUKHMe83x3O58q_eeJ9wYmdEA6Bdu2",
                "st9weeTExESWh_2SkkTJ6wpJwBxu6OFjxjSLgo5K",
                "AnuPC_gPb3fL2RBiAF5DGLSLyz-3iEOIBhVOQG0w",
                "https://twitter.com/jhysum");

        FlicManager.setAppCredentials("0affb66b-7595-4b2f-a72c-8e4f920f5744", "cbef0dda-c970-48f1-b9ad-8b960c9bfae5", "Ubly");
    }

    public AppComponent applicationComponent(){
        return mUblyApp;
    }
}
