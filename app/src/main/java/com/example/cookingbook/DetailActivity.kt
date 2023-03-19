package com.example.cookingbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    private lateinit var detailFragment: RecipeDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        this.detailFragment = supportFragmentManager.findFragmentById(R.id.detail_fragment) as RecipeDetailFragment

        val recipeId = intent.extras?.getInt(RECIPE_ID)
        if (recipeId != null) {
            this.detailFragment.setRecipeId(recipeId)
        }
    }

    companion object {
        const val RECIPE_ID = "id"
    }
}