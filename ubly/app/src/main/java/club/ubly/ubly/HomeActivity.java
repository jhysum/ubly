package club.ubly.ubly;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.neno0o.ubersdk.Activites.Authentication;
import com.neno0o.ubersdk.Endpoints.Models.UserProfile.User;
import com.neno0o.ubersdk.Uber;
import com.neno0o.ubersdk.Widgets.UberButton;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ubly.ubly.Network.LyftAuthentication;
import club.ubly.ubly.Util.BackgroundLooper;
import club.ubly.ubly.Util.LyftDao;
import club.ubly.ubly.Util.MainLooper;
import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Scheduler;

public class HomeActivity extends AppCompatActivity {

    private static final int UBER_AUTHENTICATION = 1;
    private static final int LYFT_AUTHENTICATION = 2;

//    @Bind(R.id.ride_switch)
//    MaterialAnimatedSwitch mSwitch;
//    @Bind(R.id.current_location)
//    EditText mCurrentLocation;
//    @Bind(R.id.destination)
//    EditText mDestination;
//    @Bind(R.id.fab)
//    FloatingActionButton mFab;

    @Inject
    Uber mUber;
    @Inject
    LyftDao mLyftDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        ((UblyApplication) getApplication()).applicationComponent()
                .plus(new ActivityModule(this))
                .inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("jesse", "this is the accessToken: " + Uber.getInstance().getAccessToken());
    }

    @OnClick(R.id.uberBtn)
    public void onUberButton() {
        Intent intent = new Intent(this, Authentication.class);
        startActivityForResult(intent, UBER_AUTHENTICATION);
    }
    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Intent intent = new Intent(this, LyftAuthentication.class);
        startActivityForResult(intent, LYFT_AUTHENTICATION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UBER_AUTHENTICATION) {
            // you have now access token
            // you can access resources on behalf of an Uber use
            Log.d("jesse", "This is the uber obj" + mUber.getUberAPIService().toString());
            mUber.getUberAPIService().getMe(new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    Log.d("jesse", user.getFirstName());

                    try {
                        FlicManager.getInstance(HomeActivity.this, manager -> manager.initiateGrabButton(HomeActivity.this));
                    } catch (FlicAppNotInstalledException err) {
                        Toast.makeText(HomeActivity.this, "Flic App is not installed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("jesse","this is an error: " + error) ;
                }
            });
        } else if (requestCode == LYFT_AUTHENTICATION) {
            Log.d("jesse", "this is from the lyft web site: " + mLyftDao.getUserToken());
            try {
                FlicManager.getInstance(HomeActivity.this, manager -> manager.initiateGrabButton(HomeActivity.this));
            } catch (FlicAppNotInstalledException err) {
                Toast.makeText(HomeActivity.this, "Flic App is not installed", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == FlicManager.GRAB_BUTTON_REQUEST_CODE) {
            FlicManager.getInstance(this, manager -> {
                FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
                if (button != null) {
                    button.registerListenForBroadcast(FlicBroadcastReceiverFlags.UP_OR_DOWN | FlicBroadcastReceiverFlags.REMOVED);
                    Toast.makeText(HomeActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
