package com.example.cookingbook

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cookingbook.data.AppDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val ARG_RECIPE_ID = "recipeID"

class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {
    private var recipeId: Int? = null

    override fun onStart() {
        super.onStart()

        val view = this.view
        val recipeId = this.recipeId

        if (view == null || recipeId == null) {
            return
        }

        GlobalScope.launch {
            val recipe = AppDatabase.getDatabase(requireContext()).recipeDao().getRecipe(recipeId)
            view.findViewById<TextView>(R.id.recipe_name).text = recipe.recipe.name
            view.findViewById<TextView>(R.id.recipe_difficulty).text = recipe.recipe.difficulty
            view.findViewById<TextView>(R.id.recipe_calories).text = recipe.recipe.calories.toString() + " kcal"
            view.findViewById<TextView>(R.id.recipe_cooking_time).text = recipe.recipe.cookingTime.toString() + " min"
            view.findViewById<TextView>(R.id.recipe_serving_size).text = recipe.recipe.servingSize.toString()
            view.findViewById<TextView>(R.id.cooking_steps).text =
                recipe.steps.mapIndexed { index, step -> "%d. %s".format((index + 1), step.content) }
                    .joinToString("\n\n")

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_RECIPE_ID, recipeId ?: -1)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(ARG_RECIPE_ID)
            return
        }

        var ingredientsFragment = childFragmentManager.findFragmentByTag(INGREDIENT_FRAGMENT_TAG)
            ?: IngredientsFragment.newInstance(recipeId ?: -1)

        childFragmentManager.beginTransaction()
            .replace(R.id.ingredients_fragment_container, ingredientsFragment)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(recipeId: Int) = RecipeDetailFragment().apply {
            this.recipeId = recipeId
        }

        private const val INGREDIENT_FRAGMENT_TAG = "ingredientFragment"
    }
}