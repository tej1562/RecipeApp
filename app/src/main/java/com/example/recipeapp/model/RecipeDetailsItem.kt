package com.example.recipeapp.model

data class RecipeDetailsItem(
    val imgUrl: String? = null,
    val recipeName: String? = null,
    val recipeLikes: Int? = null,
    val time: Int? = null,
    val description: String? =null
): Item(TYPE.RECIPE_DETAILS)