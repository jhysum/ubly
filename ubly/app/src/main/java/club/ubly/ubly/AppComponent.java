package club.ubly.ubly;

import android.app.Application;

import club.ubly.ubly.Network.LyftAuthService;
import club.ubly.ubly.Network.LyftAuthentication;
import club.ubly.ubly.Network.NetworkModule;
import dagger.Component;

@ApplicationScope
@Component(modules = {
        AppModule.class,
        NetworkModule.class
})
public interface AppComponent {
    void inject(UblyApplication app);
    void inject(LyftAuthentication lyftAuthentication);
    ActivityComponent plus(ActivityModule activityModule);
    LyftAuthService lyftAuthService();
    Application app();
}
