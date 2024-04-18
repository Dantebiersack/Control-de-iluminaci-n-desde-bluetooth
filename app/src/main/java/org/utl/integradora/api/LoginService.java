package org.utl.integradora.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @FormUrlEncoded
    @POST("login/buscar")
    Call<JsonObject> validar(@Field("user") String user,
                             @Field("password") String password);
}
