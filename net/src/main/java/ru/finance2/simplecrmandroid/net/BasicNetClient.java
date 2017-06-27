package ru.finance2.simplecrmandroid.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasicNetClient<T> {
    final int TIMEOUT = 120; // таймаут соединения в секундах
    final String TOKEN_KEY = "token";
    OkHttpClient client;
    protected T api;
    protected String baseUrl;
    protected String token;
    private NetError lastNetError = null;
    private String lastAnswerBody = null;
    protected Call currentCall;
    public static String DEFAULT_OK = "OK";
    public static String DEFAULT_FAIL = "FAIL";
    private Gson gson;

    public String getLastAnswerBody() {
        return lastAnswerBody;
    }

    public Gson getGson() {
        return gson;
    }

    public BasicNetClient(String baseUrl, String token, Class<T> apiClass)  {
        this.baseUrl = baseUrl;
        this.token = token;

        gson = new GsonBuilder()
                .create();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        addInterceptors(builder);
        this.client = builder.build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(this.client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(apiClass);
    }

    protected void addInterceptors(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logger);

        Interceptor tokenInjector = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(TOKEN_KEY, BasicNetClient.this.token)
                        .build();

                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        builder.addInterceptor(tokenInjector);

    }

    public NetError getLastNetError() {
        return this.lastNetError;
    }

    /**
     * Применяется для выполнения переданного Call объекта в запросах <b>на выборку массива данных</b>.<br/><br/>
     * Возвращает результат выполнения в виде списка объектов ответа в случае успеха.<br/><br/>
     * Возвращает null в случае неудачи и записывает распарсенную ошибку в this.lastNetError.<br/><br/>
     * Если распарсить ошибку не удалось - то в качестве message объекта this.lastNetError записывается "FAIL"<br/><br/>
     * @return
     * @throws IOException
     */
    protected <E> List<E> fillResultOrError(Call<List<E>> call) throws IOException {
        currentCall = call;
        retrofit2.Response<List<E>> response = call.execute();
        List<E> objects = null;
        if( response.isSuccessful() ) {
            System.out.println("Success");
            objects = response.body();
        } else {
            System.out.println("Fail");
            Gson gson = new Gson();
            NetError error = new NetError();
            try {
                error = gson.fromJson(response.errorBody().string(), NetError.class);
            } catch (Exception e) {
                error.message = "FAIL";
            }
            System.out.println("Message: "+error.message);
            this.lastNetError = error;
        }
        return objects;
    }

    protected <E> E fillOneResultOrError(Call<E> call) throws IOException {
        currentCall = call;
        retrofit2.Response<E> response = call.execute();
        E object = null;
        if( response.isSuccessful() ) {
            System.out.println("Success");
            object = response.body();
        } else {
            System.out.println("Fail");
            Gson gson = new Gson();
            NetError error = new NetError();
            try {
                error = gson.fromJson(response.errorBody().string(), NetError.class);
            } catch (Exception e) {
                error.message = "FAIL";
            }
            System.out.println("Message: "+error.message);
            this.lastNetError = error;
        }
        return object;
    }

    protected boolean sendResultOrError(Call<String> call) throws IOException {
        return sendResultOrError(call, true);
    }

    /**
     * Применяется для выполнения переданного Call объекта в запросах <b>на отправку набора данных</b>.<br/><br/>
     * Возвращает true в случае успеха (успехом считается код HTTP ответа в пределах 200-300
     * и ЛИБО когда defaultCheck == false, ЛИБО наличие в теле ответа сообщения значения константы DEFAULT_OK)<br/><br/>
     * Возвращает false в случае неудачи (при коде ответа != [200-300]) и записывает распарсенную ошибку в this.lastNetError
     * (или "FAIL" в message поля this.lastNetError, если
     * распарсить ошибку не удалось).<br/><br/>
     * Возвращает false в случае неудачи (при коде ответа = [200-300]), если тело ответа не равно значению константы DEFAULT_OK,
     * и записывает тело ответа в message поля this.lastNetError. <br/><br/>
     * @return
     * @throws IOException
     */
    protected boolean sendResultOrError(Call<String> call, boolean defaultCheck) throws IOException {
        currentCall = call;
        retrofit2.Response<String> response = call.execute();
        NetError error = new NetError();
        if( response.isSuccessful() ) {
            System.out.println("Success");
            String responseString = response.body();
            lastAnswerBody = responseString;
            if( !defaultCheck || responseString.equals(DEFAULT_OK) ) {
                return true;
            } else {
                error.message = responseString;
                this.lastNetError = error;
                return false;
            }
        } else {
            System.out.println("Fail");
            Gson gson = new Gson();
            try {
                error = gson.fromJson(response.errorBody().string(), NetError.class);
            } catch (Exception e) {
                error.message = "FAIL";
            }
            System.out.println("Message: "+error.message);
            this.lastNetError = error;
        }
        return false;
    }

    public void cancel() {
        if( currentCall != null ) {
            currentCall.cancel();
        }
    }
}
