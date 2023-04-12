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

        savedInstanceState?.let {
            recipeId = it.getInt(ARG_RECIPE_ID)
            servingSize = it.getInt(ARG_SERVING_SIZE)
        }

        val recipeId = this.recipeId ?: return

        GlobalScope.launch {
            recipe = AppDatabase.getDatabase(requireContext()).recipeDao().getRecipe(recipeId)

            if (servingSize == null) {
                servingSize = recipe?.recipe?.servingSize
            }

            renderUi()
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
        outState.putInt(ARG_RECIPE_ID, recipeId!!)
        outState.putInt(ARG_SERVING_SIZE, servingSize!!)
        super.onSaveInstanceState(outState)
    }

    private fun renderUi() {
        val handler = Handler(Looper.getMainLooper())

        handler.post {
            val view = this.view ?: return@post
            val recipe = this.recipe ?: return@post
            val servingSize = this.servingSize ?: return@post

            view.findViewById<TextView>(R.id.ingredients).text = recipe.getIngredientsString(servingSize)
            view.findViewById<TextView>(R.id.serving_size).text = servingSize.toString()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(recipeId: Int) = IngredientsFragment().apply {
            this.recipeId = recipeId
        }
    }
}