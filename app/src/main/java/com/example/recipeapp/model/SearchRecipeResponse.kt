package com.example.recipeapp.model

data class SearchRecipeResponse(
    val number: Int,
    val offset: Int,
    val results: List<Result>,
    val totalResults: Int
)