package ru.finance2.simplecrmandroid.net;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class OneWayEntityNetClient extends BasicNetClient<TwoWaysEntitiesAPI> {

    public OneWayEntityNetClient(String baseUrl, String token) {
        super(baseUrl, token, TwoWaysEntitiesAPI.class);
    }

    public List<JsonObject> getEntities() throws IOException {
        Call<List<JsonObject>> call = api.getEntities();
        return fillResultOrError(call);
    }

    public static void main(String[] args) throws IOException {
        OneWayEntityNetClient client = new OneWayEntityNetClient("http://192.168.1.38/one-way-entity-sync-api/", "1");
        List<JsonObject> entities = client.getEntities();

        System.out.println(entities);
    }

}
