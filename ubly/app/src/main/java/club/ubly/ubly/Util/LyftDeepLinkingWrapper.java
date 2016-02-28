package club.ubly.ubly.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

/**
 * Created by jessesum on 2/27/16.
 */
public class LyftDeepLinkingWrapper {

    private static final String TAG = "lyft:Example";
    private static final String LYFT_PACKAGE = "me.lyft.android";

    private final Activity mActivity;

    public LyftDeepLinkingWrapper(Activity activity) {
        mActivity = activity;
    }

    private void deepLinkIntoLyft() {
        if (isPackageInstalled(mActivity, LYFT_PACKAGE)) {
            //This intent will help you to launch if the package is already installed
            openLink(mActivity, "lyft://");

            Log.d(TAG, "Lyft is already installed on your phone, deeplinking.");
        } else {
            openLink(mActivity, "https://play.google.com/store/apps/details?id=" + LYFT_PACKAGE);

            Log.d(TAG, "Lyft is not currently installed on your phone, opening Play Store.");
        }
    }

    static void openLink(Activity activity, String link) {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
        playStoreIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        playStoreIntent.setData(Uri.parse(link));
        activity.startActivity(playStoreIntent);
    }

    static boolean isPackageInstalled(Context context, String packageId) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageId, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // ignored.
        }
        return false;
    }
}
