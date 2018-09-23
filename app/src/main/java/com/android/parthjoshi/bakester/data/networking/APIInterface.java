package com.android.parthjoshi.bakester.data.networking;

import com.android.parthjoshi.bakester.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET(Path.END_POINT)
    Call<List<Recipe>> getRecipes();
}
