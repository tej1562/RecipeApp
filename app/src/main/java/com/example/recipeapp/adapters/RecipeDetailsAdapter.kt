package com.example.recipeapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.model.InstructionsItem
import com.example.recipeapp.model.Item
import com.example.recipeapp.model.RecipeDetailsItem
import com.example.recipeapp.model.TopicItem
import kotlinx.android.synthetic.main.item_recipe_details.view.*
import kotlinx.android.synthetic.main.item_recipe_instructions.view.*
import kotlinx.android.synthetic.main.item_title.view.*


class RecipeDetailsAdapter(
    private val items: List<Item>
): RecyclerView.Adapter<RecipeDetailsAdapter.RecipeDetailsViewHolder>() {

    companion object{
        const val TYPE_RECIPE_DETAILS = 0
        const val TYPE_TITLE = 1
        const val TYPE_INSTRUCTIONS = 2
        const val TYPE_INSTRUCTIONS_TOP = 3
        const val TYPE_INSTRUCTIONS_BOTTOM = 4
    }

  inner class RecipeDetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        when(position)
        {
            0 -> return TYPE_RECIPE_DETAILS
            1 -> return TYPE_TITLE
            2 -> return TYPE_INSTRUCTIONS_TOP
            items.size - 1 -> return TYPE_INSTRUCTIONS_BOTTOM
        }
        return TYPE_INSTRUCTIONS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeDetailsViewHolder {
        val view = when(viewType){
            TYPE_RECIPE_DETAILS -> LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_details,parent,false)
            TYPE_TITLE -> LayoutInflater.from(parent.context).inflate(R.layout.item_title,parent,false)
            else -> LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_instructions,parent,false)
        }
        return RecipeDetailsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeDetailsViewHolder, position: Int) {
        val recipeDetails = items[position]
        when(holder.itemViewType){
            TYPE_RECIPE_DETAILS -> {
                holder.itemView.apply {
                    Glide.with(this).load((recipeDetails as RecipeDetailsItem).imgUrl).into(ivRecipeImage)
                    tvRecipeName.text = recipeDetails.recipeName
                    tvLikes.text = recipeDetails.recipeLikes.toString() + " Likes"
                    tvRecipeDescription.text = recipeDetails.description
                }
            }
            TYPE_TITLE -> {
                holder.itemView.apply {
                    tvTitleName.text = (recipeDetails as TopicItem).title
                }
            }
            TYPE_INSTRUCTIONS -> {
                holder.itemView.apply {
                    tvInstructionNumber.text = (recipeDetails as InstructionsItem).number.toString()
                    tvInstruction.text = recipeDetails.instruction
                }
            }
            TYPE_INSTRUCTIONS_TOP -> {
                holder.itemView.apply {
                    vLineTop.visibility = View.GONE
                    tvInstructionNumber.text = (recipeDetails as InstructionsItem).number.toString()
                    tvInstruction.text = recipeDetails.instruction
                }
            }
            TYPE_INSTRUCTIONS_BOTTOM -> {
                holder.itemView.apply{
                    vLineBottom.visibility = View.GONE
                    tvInstructionNumber.text = (recipeDetails as InstructionsItem).number.toString()
                    tvInstruction.text = recipeDetails.instruction
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


}