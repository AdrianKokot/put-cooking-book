package com.example.cookingbook

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cookingbook.data.AppDatabase
import com.example.cookingbook.data.RecipeWithStepsAndIngredients
import com.example.cookingbook.models.Recipe
import com.example.cookingbook.models.RecipeList

class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {
    private var recipe: RecipeWithStepsAndIngredients? = null
    private var recipeId: Int? = null

    fun setRecipeId(id: Int) {
        this.recipeId = id
        this.recipe = AppDatabase.getDatabase(requireContext()).recipeDao().getRecipe(id)
    }

    override fun onStart() {
        super.onStart()

        val view = this.view
        val recipe = this.recipe

        if (view == null || recipe == null) {
            return
        }

        view.findViewById<TextView>(R.id.recipe_name).text = recipe.recipe.name
        view.findViewById<TextView>(R.id.recipe_difficulty).text = recipe.recipe.difficulty
        view.findViewById<TextView>(R.id.cooking_steps).text = recipe.steps.joinToString("\n")
        view.findViewById<TextView>(R.id.ingredients).text = recipe.ingredients.joinToString("\n")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("recipeId", recipeId ?: -1)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            val recipeId = savedInstanceState.getInt("recipeId")
            if (recipeId != -1) {
                this.setRecipeId(recipeId)
            }
        }
    }

    companion object {
        fun newInstance(id: Int): Fragment {
            val fragment = RecipeDetailFragment()
            fragment.setRecipeId(id)
            return fragment
        }
    }
}