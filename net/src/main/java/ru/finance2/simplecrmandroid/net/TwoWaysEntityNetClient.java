package ru.finance2.simplecrmandroid.net;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;

public class TwoWaysEntityNetClient extends BasicNetClient<TwoWaysEntitiesAPI> {

    public TwoWaysEntityNetClient(String baseUrl, String token) {
        super(baseUrl, token, TwoWaysEntitiesAPI.class);
    }

    public List<JsonObject> getEntities() throws IOException {
        Call<List<JsonObject>> call = api.getEntities();
        return fillResultOrError(call);
    }

    public boolean pushEntities(List entitiesList) throws IOException {
        Call<String> call = api.pushEntities(getGson().toJson(entitiesList));
        return sendResultOrError(call);
    }

    public static void main(String[] args) throws IOException {
        TwoWaysEntityNetClient client = new TwoWaysEntityNetClient("http://192.168.1.38/two-ways-entity-sync-api/", "1");
        List<JsonObject> entities = client.getEntities();

        System.out.println(entities);
    }

}
