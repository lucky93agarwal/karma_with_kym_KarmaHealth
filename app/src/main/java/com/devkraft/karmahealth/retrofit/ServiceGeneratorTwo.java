package com.devkraft.karmahealth.retrofit;

import static com.devkraft.karmahealth.retrofit.Log.LOG;

import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGeneratorTwo {
    public static String URL_WEB = "http://staging-karmahealth.knowyourmeds.com:8899/";  // please staging server

    public static String URL_OTP_WEB = "https://2factor.in/API/V1/d2e21f47-633a-11e8-a895-0200cd936042/SMS/"; // otp and otp check
    public static final String Privacy = URL_WEB + "index.php/c_utama/privacy";
    public static final String FAQ = URL_WEB + "index.php/c_utama/faq";
    public static final String TOS = URL_WEB + "index.php/c_utama/terms_conditions";
    public static final String FORGOT = URL_WEB + "index.php/c_utama/forgot_password_customer";

    public static final String API_BASE_URL = URL_WEB;


    private static BooleanSerializerDeserializer booleanSerializerDeserializer = new BooleanSerializerDeserializer();
    public static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            .registerTypeAdapter(Boolean.class, booleanSerializerDeserializer)
            .registerTypeAdapter(boolean.class, booleanSerializerDeserializer)
            .create();


    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit.Builder builderOTP =
            new Retrofit.Builder()
                    .baseUrl(URL_OTP_WEB)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit.Builder builderLogin =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null, null,false);
    }

    public static <S> S createService(Class<S> serviceClass, String username, String password,Boolean otp) {
        if (!httpClient.interceptors().isEmpty()) {
            httpClient.interceptors().clear();
        }
        if (username != null && password != null) {

            String credentials = username + ":" + password;
            android.util.Log.d("walletwallets","credentials= "+credentials);
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            Log.d("walletwallets","basic= "+basic);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", basic)
                            .header("Accept", "application/json")
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }


        // Logging HTTP Request & Response
        if (LOG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit;
        if(otp){
            retrofit = builderOTP.client(client).build();

        }else {
            retrofit = builder.client(client).build();
        }
        return retrofit.create(serviceClass);
    }
}
