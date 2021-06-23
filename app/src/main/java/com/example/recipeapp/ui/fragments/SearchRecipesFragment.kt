package com.example.recipeapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.SearchRecipeAdapter
import com.example.recipeapp.ui.RecipeActivity
import com.example.recipeapp.ui.RecipeViewModel
import com.example.recipeapp.util.Constants.Companion.SEARCH_RECIPE_DELAY
import com.example.recipeapp.util.Resource
import kotlinx.android.synthetic.main.fragment_search_recipes.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchRecipesFragment : Fragment(R.layout.fragment_search_recipes) {
    lateinit var viewModel: RecipeViewModel
    lateinit var recipeAdapter: SearchRecipeAdapter
    val TAG = "SearchRecipesFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as RecipeActivity).recipeViewModel
        setupRecyclerView()

        recipeAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("result", it)
            }
            findNavController().navigate(
                R.id.action_searchRecipesFragment_to_recipeDetailsFragment,
                bundle
            )
        }

        var job: Job? = null

        etSearchRecipes.addTextChangedListener{editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_RECIPE_DELAY)
                editable?.let{
                    if(editable.toString().isNotEmpty())
                    {
                        viewModel.searchForRecipes(it.toString())
                    }
                }
            }
        }

        viewModel.searchRecipes.observe(viewLifecycleOwner, Observer {response ->
            when(response)
            {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {recipeResponse ->
                        recipeAdapter.differ.submitList(recipeResponse.results)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let{
                        Log.e(TAG,"An error occcured : $it")
                    }
                }

                is Resource.Loading -> {
                    hideInfoLayout()
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar(){
        paginationProgressBar.visibility = View.GONE
    }

    private fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun hideInfoLayout(){
        infoLayout.visibility = View.GONE
    }

    private fun setupRecyclerView(){
        recipeAdapter = SearchRecipeAdapter()
        rvSearchRecipes.apply {
            adapter =  recipeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}