package club.ubly.ubly;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import javax.inject.Named;

import club.ubly.ubly.Util.BackgroundLooper;
import club.ubly.ubly.Util.MainLooper;
import dagger.Module;
import dagger.Provides;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application application){
        mApplication = application;
    }

    @ApplicationScope
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @ApplicationScope @Provides
    public SharedPreferences provideSharedPreference(Application application){
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @ApplicationScope @Provides
    public SharedPreferences.Editor provideSharedPreferenceEditor(SharedPreferences sharedPreferences){
        return sharedPreferences.edit();
    }

    @Provides @BackgroundLooper
    Scheduler providesBackgroundScheduler() {
        return Schedulers.io();
    }

    @Provides @MainLooper
    Scheduler providesMainScheduler() {return AndroidSchedulers.mainThread();}

    @Provides @ApplicationScope
    PackageManager providesPackageManager(Application application) {
        return application.getPackageManager();
    }

    @Provides @ApplicationScope @Named("packageName")
    String providesPackageName(Application application) {
        return application.getPackageName();
    }
}

