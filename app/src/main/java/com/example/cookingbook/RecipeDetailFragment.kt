package com.example.cookingbook

import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cookingbook.models.Recipe
import com.example.cookingbook.models.RecipeList

class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {
    var recipe: Recipe? = null

    fun setRecipeId(id: Int) {
        this.recipe = RecipeList.recipes[id]
    }

    override fun onStart() {
        super.onStart()

        val view = this.view
        val recipe = this.recipe

        if (view == null || recipe == null) {
            return
        }

        view.findViewById<TextView>(R.id.recipe_name).text = recipe.name
        view.findViewById<TextView>(R.id.recipe_difficulty).text = recipe.difficulty
        view.findViewById<TextView>(R.id.cooking_steps).text = recipe.cookingSteps.joinToString("\n")
        view.findViewById<TextView>(R.id.ingredients).text = recipe.ingredients.joinToString("\n")
    }
}