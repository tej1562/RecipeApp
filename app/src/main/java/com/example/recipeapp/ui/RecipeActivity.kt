package com.example.recipeapp.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.recipeapp.R
import com.example.recipeapp.repository.RecipeRepository
import kotlinx.android.synthetic.main.activity_recipe.*


class RecipeActivity : AppCompatActivity() {

    lateinit var recipeViewModel: RecipeViewModel
    val SHARED_PREFS = "sharedPrefs"
    val TEXT = "text"
    val SWITCH = "switch"
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        sharedPref = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        bottomNavigationView.setupWithNavController(recipeNavHostFragment.findNavController())
        val repository = RecipeRepository()
        val viewModelProviderFactory = RecipeViewModelProviderFactory(application,repository)
        recipeViewModel = ViewModelProvider(this, viewModelProviderFactory).get(RecipeViewModel::class.java)

    }

    fun saveState(isChecked: Boolean){
        val editor = sharedPref.edit()
        editor.apply{
            putBoolean("isDark",isChecked)
            apply()
        }
    }

    fun loadState(): Boolean{
       val isDark = sharedPref.getBoolean("isDark", false)
       return isDark
   }


}