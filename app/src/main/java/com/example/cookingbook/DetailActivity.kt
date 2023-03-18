package com.example.cookingbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class DetailActivity : AppCompatActivity() {
    private lateinit var detailFragment: RecipeDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        this.detailFragment = supportFragmentManager.findFragmentById(R.id.detail_fragment) as RecipeDetailFragment
        this.detailFragment.setRecipeId(1)
    }
}