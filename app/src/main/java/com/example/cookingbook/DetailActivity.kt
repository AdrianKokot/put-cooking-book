package com.example.cookingbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.cookingbook.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val recipeId = intent.extras?.getInt(RECIPE_ID)

            if (recipeId != null) {
                val detailFragment =
                    supportFragmentManager.findFragmentByTag(DETAIL_FRAGMENT_TAG)
                        ?: RecipeDetailFragment.newInstance(recipeId)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .commit()
            }
        }

        setContentView(R.layout.activity_detail)
    }

    companion object {
        const val RECIPE_ID = "id"
        private const val DETAIL_FRAGMENT_TAG = "detail_fragment"
    }
}