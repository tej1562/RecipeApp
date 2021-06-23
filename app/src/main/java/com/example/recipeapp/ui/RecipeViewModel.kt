package com.example.recipeapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.RecipeApplication
import com.example.recipeapp.model.RecipeResponse
import com.example.recipeapp.model.SearchRecipeResponse
import com.example.recipeapp.repository.RecipeRepository
import com.example.recipeapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class RecipeViewModel(
    app: Application,
    val repository: RecipeRepository
): AndroidViewModel(app) {

    val newRecipes: MutableLiveData<Resource<RecipeResponse>> = MutableLiveData()
    val numberNewRecipes = 20

    val searchRecipes: MutableLiveData<Resource<SearchRecipeResponse>> = MutableLiveData()
    val numberSearchRecipes = 20

    init {
        getNewRecipes()
    }

    fun getNewRecipes() = viewModelScope.launch{
       safeNewRecipeCall()
    }

    fun searchForRecipes(query: String) = viewModelScope.launch{
        safeSearchRecipeCall(query)
    }

    private suspend fun safeNewRecipeCall(){
        newRecipes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.getRecipes(numberNewRecipes)
                newRecipes.postValue(handleNewRecipeResponse(response))
            } else {
                newRecipes.postValue(Resource.Error("No Internet Connection"))
            }
        }
        catch (t: Throwable){
            when(t){
                is IOException -> newRecipes.postValue(Resource.Error("Network Failure"))
                else -> newRecipes.postValue(Resource.Error("Response Conversion Error"))
            }
        }
    }

    private suspend fun safeSearchRecipeCall(query: String){
        searchRecipes.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.searchRecipes(numberSearchRecipes,query)
                searchRecipes.postValue(handleSearchRecipeResponse(response))
            } else {
                searchRecipes.postValue(Resource.Error("No Internet Connection"))
            }
        }
        catch (t: Throwable){
            when(t){
                is IOException -> searchRecipes.postValue(Resource.Error("Network Failure"))
                else -> searchRecipes.postValue(Resource.Error("Response Conversion Error"))
            }
        }
    }


    private fun handleNewRecipeResponse(response: Response<RecipeResponse>): Resource<RecipeResponse>{
        if(response.isSuccessful)
        {
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message(),response.body())
    }

    private fun handleSearchRecipeResponse(response: Response<SearchRecipeResponse>): Resource<SearchRecipeResponse>{
        if(response.isSuccessful)
        {
            response.body()?.let {resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message(),response.body())
    }

    fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<RecipeApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else{
          connectivityManager.activeNetworkInfo?.run{
              return when(type){
                  TYPE_WIFI -> true
                  TYPE_MOBILE -> true
                  TYPE_ETHERNET -> true
                  else -> false
              }
          }
        }
        return false
    }
}