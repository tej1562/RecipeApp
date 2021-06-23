package com.example.recipeapp.api

import com.example.recipeapp.model.RecipeResponse
import com.example.recipeapp.model.SearchRecipeResponse
import com.example.recipeapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeAPI {

    @GET("recipes/random")
    suspend fun getRecipes(
            @Query("number")
            number: Int = 20,
            @Query("tags")
            query: String,
            @Query("apiKey")
            apiKey: String = API_KEY
        ): Response<RecipeResponse>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("number")
        number: Int = 20,
        @Query("query")
        query: String,
        @Query("addRecipeInformation")
        addRecipeInformation: Boolean = true,
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<SearchRecipeResponse>


}
