package club.ubly.ubly.Network;

import com.neno0o.ubersdk.Uber;

import club.ubly.ubly.ApplicationScope;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by jessesum on 2/27/16.
 */
@Module
public class NetworkModule {

    @Provides
    @ApplicationScope
    Retrofit providesLyftRetroFit(RestClient restClient) {
        try {
            return restClient.createLyftRetrofit("https://api.lyft.com/v1/", "D35DoULU-GiX", "zQeyMTdf9Y6KzRLheLUYWYCY5gprFkhu");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Provides
    @ApplicationScope
    public LyftAuthService provideLyftService(RestClient restClient, Retrofit retrofit){
        return restClient.createLyftService(retrofit);
    }

    @Provides
    @ApplicationScope
    public Uber providesUber() {
        return Uber.getInstance();
    }
}
