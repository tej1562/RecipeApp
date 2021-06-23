package com.example.recipeapp.repository


import com.example.recipeapp.api.RetrofitInstance


class RecipeRepository {

    suspend fun getRecipes(number: Int) = RetrofitInstance.api.getRecipes(number,"vegetarian")

    suspend fun searchRecipes(number: Int,query: String) = RetrofitInstance.api.searchRecipes(number,query)

}