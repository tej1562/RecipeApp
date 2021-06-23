package com.example.recipeapp.model

abstract class Item(val type: String) {
    class TYPE{
        companion object{
            val RECIPE_DETAILS = "recipe"
            val TOPIC = "topic"
            val INSTRUCTIONS = "instructions"
        }
    }
}