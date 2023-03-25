package com.example.cookingbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var detailFragment: RecipeDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeId = intent.extras?.getInt(RECIPE_ID)
        if (recipeId != null) {

            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragment_container, RecipeDetailFragment.newInstance(recipeId))
            transaction.commit()
        }

        setContentView(R.layout.activity_detail)
    }

    companion object {
        const val RECIPE_ID = "id"
    }
}