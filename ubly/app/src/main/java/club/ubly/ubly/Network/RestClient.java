package club.ubly.ubly.Network;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;

import club.ubly.ubly.ApplicationScope;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

@ApplicationScope
public class RestClient {
    static final String LOGGER_TAG = RestClient.class.getCanonicalName();
    private static final boolean LOG_REQUEST_RESPONSE = true;

    @Inject
    public RestClient() {}

    public Retrofit createLyftRetrofit(String baseUrl, String clientId, String clientSecret) throws ClassNotFoundException {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();

        if (LOG_REQUEST_RESPONSE) {
            addLoggingInterceptor(httpClientBuilder);
        }

        addLyftAuthToken(httpClientBuilder, clientId, clientSecret);

        OkHttpClient okHttpClient = httpClientBuilder
                .build();

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .build();
    }

    public Retrofit createLyftApiRetrofit(String baseUrl, String accessToken) throws ClassNotFoundException {

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient().newBuilder();

        if (LOG_REQUEST_RESPONSE) {
            addLoggingInterceptor(httpClientBuilder);
        }

        addLyftBearerToken(httpClientBuilder, accessToken);

        OkHttpClient okHttpClient = httpClientBuilder
                .build();

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .build();
    }

    public LyftAuthService createLyftService(Retrofit retrofit) {
        return retrofit.create(LyftAuthService.class);
    }

    public LyftApiService createLyftUserService(Retrofit retrofit) {
        return retrofit.create(LyftApiService.class);
    }

    private void addLoggingInterceptor(OkHttpClient.Builder httpClientBuilder) {
        httpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Log.d(LOGGER_TAG, String.format("Sending request %s on %s%n%s\n",
                    request.url(), chain.connection(), request.headers()));
            if (request.body() != null) {
                Buffer sink = new Buffer();
                request.body().writeTo(sink);
                Log.d(LOGGER_TAG, "Request Body: " + sink.readUtf8());
            }

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            Log.d(LOGGER_TAG, String.format("Received %d response for %s in %.1fms%n%s\n",
                    response.code(),
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            if (response.body() != null) {

                String bodyString = response.body().string();
                Log.d(LOGGER_TAG, String.format("Received response body: %s", bodyString));

                return response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), bodyString))
                        .build();
            }

            return response;
        });
    }

    private void addLyftAuthToken(OkHttpClient.Builder httpClientBuilder, String clientId, String clientSecret) {
        httpClientBuilder.addInterceptor(chain -> {
            Request original = chain.request();

            byte[] credentials = (clientId+":"+clientSecret).getBytes();
            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json" )
                    .header("Authorization", "Basic " + Base64.encodeToString(credentials, Base64.NO_WRAP))
                    .method(original.method(), original.body());


            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
    }

    private void addLyftBearerToken(OkHttpClient.Builder httpClientBuilder, String accessToken) {
        httpClientBuilder.addInterceptor(chain -> {
            Request original = chain.request();

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json" )
                    .header("Authorization", "Bearer " + accessToken.trim())
                    .method(original.method(), original.body());


            Request request = requestBuilder.build();
            return chain.proceed(request);
        });
    }
}
