package com.example.cookingbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cookingbook.data.AppDatabase
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val ARG_RECIPE_ID = "recipeID"

class RecipeDetailFragment : Fragment(R.layout.fragment_recipe_detail) {
    private var recipeId: Int? = null
    private var recipeIngredientsShare: String = ""

    override fun onStart() {
        super.onStart()

        val view = this.view
        val recipeId = this.recipeId

        if (view == null || recipeId == null) {
            return
        }

        AppDatabase.getDatabase(requireContext())
            .recipeDao()
            .getRecipe(recipeId)
            .observe(viewLifecycleOwner) { recipe ->
                recipeIngredientsShare = "Recipe: ${recipe.recipe.name}\nIngredients:\n${recipe.getIngredientsString()}"
                view.findViewById<TextView>(R.id.recipe_name).text = recipe.recipe.name
                view.findViewById<TextView>(R.id.recipe_difficulty).text = recipe.recipe.difficulty
                view.findViewById<TextView>(R.id.recipe_calories).text = "${recipe.recipe.calories} kcal"
                view.findViewById<TextView>(R.id.recipe_cooking_time).text = "${recipe.recipe.cookingTime} min"
                view.findViewById<TextView>(R.id.recipe_serving_size).text = recipe.recipe.servingSize.toString()
                view.findViewById<TextView>(R.id.cooking_steps).text = recipe.getStepsString()
                view.findViewById<FloatingActionButton?>(R.id.fab)?.setOnClickListener(::onFabClick)
                view.findViewById<MaterialToolbar?>(R.id.topAppBar)?.let { toolbar ->
                    toolbar.title = recipe.recipe.name
                    view.findViewById<ImageView>(R.id.toolbar_recipe_image).setImageResource(recipe.recipe.imageId)
                    toolbar.setNavigationOnClickListener {
                        requireActivity().finish()
                    }
                }
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

        val ingredientsFragment = childFragmentManager.findFragmentByTag(INGREDIENT_FRAGMENT_TAG)
            ?: IngredientsFragment.newInstance(recipeId ?: -1)

        val timerFragment = childFragmentManager.findFragmentByTag(TIMER_FRAGMENT_TAG)
            ?: TimerFragment.newInstance()

        childFragmentManager.beginTransaction()
            .replace(R.id.ingredients_fragment_container, ingredientsFragment)
            .replace(R.id.timer_fragment_container, timerFragment)
            .commit()
    }

    private fun onFabClick(view: View) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, recipeIngredientsShare)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share recipe")
        startActivity(shareIntent)
    }

    companion object {
        @JvmStatic
        fun newInstance(recipeId: Int) = RecipeDetailFragment().apply {
            this.recipeId = recipeId
        }

        private const val INGREDIENT_FRAGMENT_TAG = "ingredientFragment"
        private const val TIMER_FRAGMENT_TAG = "timerFragment"
    }
}