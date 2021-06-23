package com.example.recipeapp.ui.fragments


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.RecipeDetailsAdapter
import com.example.recipeapp.model.InstructionsItem
import com.example.recipeapp.model.Item
import com.example.recipeapp.model.RecipeDetailsItem
import com.example.recipeapp.model.TopicItem
import kotlinx.android.synthetic.main.fragment_recipe_details.*


class RecipeDetailsFragment : Fragment(R.layout.fragment_recipe_details) {
    val args: RecipeDetailsFragmentArgs by navArgs()
    lateinit var recipeDetailsAdapter: RecipeDetailsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = mutableListOf<Item>()

        args.recipe?.let{result ->
            list.add(
                RecipeDetailsItem(
                    result.image, result.title, result.aggregateLikes,result.readyInMinutes,
                    HtmlCompat.fromHtml(result.summary, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                ),
            )
            if(result.analyzedInstructions.isNotEmpty()){
                val instructions = result.analyzedInstructions[0].steps
                if(instructions.isNotEmpty()) {
                    list.add(TopicItem("Instructions"))
                    for (instruction in instructions) {
                        list.add(InstructionsItem(instruction.number, instruction.step))
                    }
                }
            }
            else{
                Toast.makeText(activity,"Details Not Available",Toast.LENGTH_LONG) .show()
            }

        }


        args.result?.let{
            list.add(
                RecipeDetailsItem(
                    it.image, it.title, it.aggregateLikes,it.readyInMinutes,
                    HtmlCompat.fromHtml(it.summary, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
                ),
            )

            val instructions = it.analyzedInstructions[0].steps
            if(instructions.isNotEmpty()) {
                list.add(TopicItem("Instructions"))
                for (instruction in instructions) {
                    list.add(InstructionsItem(instruction.number,instruction.step))
                }
            }
        }

        setupRecyclerView(list)
    }


    private fun setupRecyclerView(details: List<Item>){
        recipeDetailsAdapter = RecipeDetailsAdapter(details)
        rvRecipeDetails.apply {
            adapter = recipeDetailsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}