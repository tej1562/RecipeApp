package com.example.recipeapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.recipeapp.R
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.ui.DrawableAlwaysCrossFadeFactory
import kotlinx.android.synthetic.main.item_recipe_preview.view.*

class RecipeAdapter: RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    inner class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object: DiffUtil.ItemCallback<Recipe>(){
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
          return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_preview,parent,false)
        return RecipeViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = differ.currentList[position]
     holder.itemView.apply{
         Glide.with(this).load(recipe.image)
             .transition(DrawableTransitionOptions.with(DrawableAlwaysCrossFadeFactory()))
             .into(ivRecipe)
         tvTitle.text = recipe.title
         val summary = HtmlCompat.fromHtml(recipe.summary,HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
         tvSummary.text = summary
         setOnClickListener{
            onItemClickListener?.let {
                it(recipe)
            }
         }
     }
    }


    override fun getItemCount(): Int {
     return differ.currentList.size
    }

    private var onItemClickListener: ((Recipe) -> Unit)? = null

    fun setOnItemClickListener(listener: (Recipe) -> Unit){
        onItemClickListener = listener
    }
}