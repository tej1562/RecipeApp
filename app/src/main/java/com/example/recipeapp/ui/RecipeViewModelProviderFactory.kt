package com.example.recipeapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.repository.RecipeRepository

class RecipeViewModelProviderFactory(
    val app: Application,
    val repository: RecipeRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecipeViewModel(app,repository) as T
    }
}

