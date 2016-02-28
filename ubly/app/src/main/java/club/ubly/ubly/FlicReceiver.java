package club.ubly.ubly;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.neno0o.ubersdk.Endpoints.Models.Products.Product;
import com.neno0o.ubersdk.Endpoints.Models.Products.Products;
import com.neno0o.ubersdk.Endpoints.Models.Requests.UberRequest;
import com.neno0o.ubersdk.Endpoints.Models.Requests.UberRequestBody;
import com.neno0o.ubersdk.Endpoints.Service.UberAPIService;
import com.neno0o.ubersdk.Uber;

import java.util.Date;

import club.ubly.ubly.Util.LyftDao;
import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FlicReceiver extends FlicBroadcastReceiver {

    private LyftDao mLyftDao;
    private UberAPIService mUber;

    @Override
    protected void onRequestAppCredentials(Context context) {
    }

    @Override
    public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
        mLyftDao = ((UblyApplication) context.getApplicationContext()).mLyftDao;
        mUber = Uber.getInstance().getUberAPIService();
        Log.d("jesse", "This is the uber obj" + mUber.toString());
        if (isUp) {

        } else {
            UberRequestBody uberRequestBody = new UberRequestBody("a1111c8c-c720-46c3-8534-2fcdd730040d",
                    37.805985,
                    -122.431896,
                    37.7655586,
                    -122.4717339,
                    "");

            //BlackButton
            if (button.getButtonId().equals("80:e4:da:70:0a:f2")){
                mUber.getProducts(37.805985, -122.431896, new Callback<Products>() {
                    @Override
                    public void success(Products products, Response response) {
                        for (Product product: products.getProducts()) {
                            Log.d("jesse", "this is the product " + product.getProductId());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });

                Log.d("jesse", "this is posting for uber");
                mUber.postRequest(uberRequestBody, new Callback<UberRequest>() {
                    @Override
                    public void success(UberRequest uberRequest, Response response) {
                        Log.d("jesse", "This si the request: " + uberRequest.getRequestId());
                        removeRequest(uberRequest);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("jesse", "this is the uber post error : " + error.getMessage());
                    }
                });
            } else if(button.getButtonId().equals("80:e4:da:70:84:56")) {
                Log.d("jesse", "this is inside the lyft post");
                mLyftDao.postRide().subscribe(ridePostResponse -> {
                    Log.d("jesse", "This si the ride post response: " + ridePostResponse.getRideID());

                    mLyftDao.cancelRide(ridePostResponse.getRideID()).subscribe(cancelPostResponse -> {
                        Log.d("jesse", "cancel successful ");
                    }, throwable -> {
                        Log.d("jesse", "cancel error " + throwable.getMessage());
                    });
                }, throwable -> {

                });
            }

            Log.d("jesse", "this si the button: " + button.getButtonId());
            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Button was pressed")
                    .setContentText("Pressed last time at " + new Date())
                    .build();
            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification);
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Log.d("yo", "removed");
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }

    private void removeRequest(UberRequest uberRequest) {
        mUber.deleteRequest(uberRequest.getRequestId(), new Callback<com.squareup.okhttp.Response>() {
            @Override
            public void success(com.squareup.okhttp.Response response, Response response2) {
                Log.d("jesse", "This is the response: " + response.message());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("jesse", "this is the uber cancel error : " + error.getMessage());
            }
        });
    }
}