package com.example.recipeapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.RecipeAdapter
import com.example.recipeapp.ui.RecipeActivity
import com.example.recipeapp.ui.RecipeViewModel
import com.example.recipeapp.util.Resource
import kotlinx.android.synthetic.main.fragment_new_recipes.*

class NewRecipesFragment : Fragment(R.layout.fragment_new_recipes) {
    lateinit var viewModel: RecipeViewModel
    lateinit var recipeAdapter: RecipeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as RecipeActivity).recipeViewModel
        setupRecyclerView()

        val checkState = (activity as RecipeActivity).loadState()

        if(checkState){
            enableDarkMode()
        }

        btnSwitch.isChecked = checkState

        btnSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            (activity as RecipeActivity).saveState(isChecked)
            if (isChecked) {
                enableDarkMode()
            } else {
               disableDarkMode()
            }
        }

        btnRetry.setOnClickListener {
            if(viewModel.hasInternetConnection())
            {
               errorLayout.visibility = View.GONE
                viewModel.getNewRecipes()
            }
        }

        recipeAdapter.setOnItemClickListener {
         val bundle = Bundle().apply {
             putSerializable("recipe", it)
         }
            findNavController().navigate(
                R.id.action_newRecipesFragment_to_recipeDetailsFragment,
                bundle
            )
        }


        viewModel.newRecipes.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    tvError.visibility = View.GONE
                    hideProgressBar()
                    response.data?.let { recipeResponse ->
                        recipeAdapter.differ.submitList(recipeResponse.recipes)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {
                        errorLayout.visibility = View.VISIBLE
                        tvError.text = it
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun enableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun disableDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }


    private fun hideProgressBar()
    {
        paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar()
    {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView(){
        recipeAdapter = RecipeAdapter()
        rvRecipes.apply {
            adapter =  recipeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}