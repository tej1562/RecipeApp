package com.example.recipeapp.model

data class StepX(
    val equipment: List<EquipmentX>,
    val ingredients: List<Ingredient>,
    val length: Length,
    val number: Int,
    val step: String
)