package com.example.cookingbook

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cookingbook.data.AppDatabase
import com.example.cookingbook.data.entities.RecipeWithStepsAndIngredients
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val ARG_RECIPE_ID = "recipeID"
private const val ARG_SERVING_SIZE = "servingSize"

class IngredientsFragment : Fragment() {
    private var recipe: RecipeWithStepsAndIngredients? = null
    private var servingSize: Int? = null
    private var recipeId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipeId = it.getInt(ARG_RECIPE_ID)
            servingSize = it.getInt(ARG_SERVING_SIZE)
            recipeId = if (recipeId == -1) null else recipeId
            servingSize = if (servingSize == -1) null else servingSize
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)

        view.findViewById<Button>(R.id.increment_serving_size).setOnClickListener {
            servingSize = servingSize?.plus(1)
            renderUi()
        }

        view.findViewById<Button>(R.id.decrement_serving_size).setOnClickListener {
            servingSize = if (servingSize ?: 0 <= 1) 1 else servingSize?.minus(1)
            renderUi()
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(ARG_RECIPE_ID, recipeId ?: -1)
        outState.putInt(ARG_SERVING_SIZE, servingSize ?: -1)
        super.onSaveInstanceState(outState)
    }

    private fun renderUi() {
        val handler = Handler(Looper.getMainLooper())

        handler.post {
            val view = this.view ?: return@post
            val recipe = this.recipe ?: return@post
            val servingSize = this.servingSize ?: return@post

            view.findViewById<TextView>(R.id.ingredients).text =
                recipe.ingredients.mapIndexed { index, ingredient ->
                    "%d. %s | %.2f %s\n\t%s".format(
                        index + 1,
                        ingredient.name,
                        ingredient.amount * servingSize / recipe.recipe.servingSize,
                        ingredient.unit,
                        ingredient.comment
                    )
                }.joinToString("\n")

            view.findViewById<TextView>(R.id.serving_size).text = servingSize.toString()
        }
    }

    override fun onStart() {
        super.onStart()

        if (recipe != null) {
            return
        }

        val recipeId = this.recipeId ?: return

        GlobalScope.launch {
            recipe = AppDatabase.getDatabase(requireContext()).recipeDao().getRecipe(recipeId)
            servingSize = recipe?.recipe?.servingSize
            renderUi()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(recipeId: Int) =
            IngredientsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_RECIPE_ID, recipeId)
                }
            }
    }
}