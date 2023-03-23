package com.example.cookingbook

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cookingbook.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {
    private var recipeId: Int? = null

    fun setRecipeId(id: Int) {
        this.recipeId = id
    }

    override fun onStart() {
        super.onStart()

        val view = this.view
        val recipeId = this.recipeId

        if (view == null || recipeId == null) {
            return
        }

        GlobalScope.launch(Dispatchers.Default) {
            launch(Dispatchers.Default) {
                val recipe = AppDatabase.getDatabase(requireContext()).recipeDao().getRecipe(recipeId)
                view.findViewById<TextView>(R.id.recipe_name).text = recipe.recipe.name
                view.findViewById<TextView>(R.id.recipe_difficulty).text = recipe.recipe.difficulty
                view.findViewById<TextView>(R.id.recipe_calories).text = recipe.recipe.calories.toString() + " kcal"
                view.findViewById<TextView>(R.id.recipe_cooking_time).text =
                    recipe.recipe.cookingTime.toString() + " min"
                view.findViewById<TextView>(R.id.recipe_serving_size).text = recipe.recipe.servingSize.toString()
                view.findViewById<TextView>(R.id.cooking_steps).text =
                    recipe.steps.mapIndexed { index, step -> "%d. %s".format((index + 1), step.content) }
                        .joinToString("\n\n")
                view.findViewById<TextView>(R.id.ingredients).text =
                    recipe.ingredients.mapIndexed { index, ingredient ->
                        "%d. %s | %.2f %s\n\t%s".format(
                            index + 1,
                            ingredient.name,
                            ingredient.amount,
                            ingredient.unit,
                            ingredient.comment
                        )
                    }.joinToString("\n")
            }
        }

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