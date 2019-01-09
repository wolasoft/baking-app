package com.wolasoft.bakingapp.data.repositories;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.bakingapp.data.models.Recipe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeRepository {

    private static final String JSON_FILE_NAME = "data.json";
    private static final Object LOCK = new Object();
    private static RecipeRepository instance = null;
    private final List<Recipe> recipes;


    private RecipeRepository(Context context) {
        Gson gson = new Gson();
        String json = this.readJsonFile(context);
        recipes = new ArrayList<>(Arrays.asList(gson.fromJson(json, Recipe[].class)));
    }

    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                instance = new RecipeRepository(context);
            }
        }
        return instance;
    }

    private String readJsonFile(Context context) {
        InputStream inputStream;
        InputStreamReader streamReader;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            inputStream = context.getResources().getAssets().open(JSON_FILE_NAME);
            streamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(streamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            streamReader.close();
            bufferedReader.close();

        } catch (Exception error) {
            error.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public List<Recipe> getAll() {
        return recipes;
    }
}
