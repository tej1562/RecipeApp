package com.example.recipeapp.model

data class InstructionsItem(
    val number: Int,
    val instruction: String
): Item(TYPE.INSTRUCTIONS)