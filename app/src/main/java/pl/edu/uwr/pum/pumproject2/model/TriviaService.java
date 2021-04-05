package pl.edu.uwr.pum.pumproject2.model;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TriviaService {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://opentdb.com/";
    private final TriviaAPI api;

    public TriviaService() {
        api = getClient().create(TriviaAPI.class);
    }

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    public Single<TriviaCategory> getCategories() {
        return api.getCategories();
    }

    public Single<TriviaQuestions> getQuestions(int amount, int category) {
        return api.getQuestions(amount, category, "multiple");
    }
}
