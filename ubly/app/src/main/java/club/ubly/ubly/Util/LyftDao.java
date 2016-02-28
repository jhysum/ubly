package club.ubly.ubly.Util;

import android.util.Log;

import javax.inject.Inject;

import club.ubly.ubly.ApplicationScope;
import club.ubly.ubly.Network.LyftApiService;
import club.ubly.ubly.Network.RestClient;
import rx.Observable;
import rx.Scheduler;

/**
 * Created by jessesum on 2/27/16.
 */
@ApplicationScope
public class LyftDao {

    private final RestClient mRestClient;
    private final Scheduler mMainScheduler;
    private final Scheduler mBackgroundScheduler;

    private LyftApiService mLyftApiService;
    private String mAccessToken;
    private String mUserToken;

    @Inject
    public LyftDao(RestClient restClient, @MainLooper Scheduler mainScheduler,
                   @BackgroundLooper Scheduler backgroundScheduler) {
        mRestClient = restClient;
        mMainScheduler = mainScheduler;
        mBackgroundScheduler = backgroundScheduler;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
//        mLyftApiService = mRestClient.createLyftApiRetrofit()
    }

    public void setUserToken(String userToken) {
        mUserToken = userToken;
        try {
            mLyftApiService = mRestClient.createLyftUserService(mRestClient.createLyftApiRetrofit("https://api.lyft.com/v1/", userToken));
            Log.d("jesse", "this is wehere it is being set: " +mLyftApiService);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String getUserToken() {
        return mUserToken;
    }

    public Observable<RidePostResponse> postRide() {
        Log.d("jesse", "This si already set: " +mLyftApiService);
        return mLyftApiService.postRide(new RideRequest("lyft", new origin(37.805985, -122.431896, " 2 Marina Blvd, San Francisco, CA 94123"),
                new destination(37.7655586, -122.4717339, "1200 12th ave, San Francisco, CA 94122")))
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler);
    }

    public Observable<CancelPostResponse> cancelRide(int rideId) {
        return mLyftApiService.cancelRide(rideId)
                .subscribeOn(mBackgroundScheduler)
                .observeOn(mMainScheduler);
    }

}
