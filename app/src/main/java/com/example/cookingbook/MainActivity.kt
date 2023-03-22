package com.example.cookingbook

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), RecipeListFragment.Companion.Listener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun itemClicked(id: Long) {
        val fragmentContainer = findViewById<View>(R.id.fragment_container)

        if (fragmentContainer != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, RecipeDetailFragment.newInstance(id.toInt()))
            transaction.addToBackStack(null)
            transaction.setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.commit()
        } else {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.RECIPE_ID, id.toInt())
            startActivity(intent)
        }
    }
}