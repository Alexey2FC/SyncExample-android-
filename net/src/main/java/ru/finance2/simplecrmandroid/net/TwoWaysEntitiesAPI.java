package ru.finance2.simplecrmandroid.net;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface TwoWaysEntitiesAPI {

    @GET("get-entities")
    Call<List<JsonObject>> getEntities();

    @FormUrlEncoded
    @POST("push-entities")
    Call<String> pushEntities(@Field("entities") String entitiesJson);

}
