package com.wolasoft.bakingapp.data.api.services;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
    @GET("baking.json")
    Call<JsonArray> getAll();
}
