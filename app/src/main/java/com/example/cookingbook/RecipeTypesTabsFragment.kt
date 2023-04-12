package com.example.cookingbook

import android.os.Bundle
import android.util.Log.d
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.cookingbook.data.entities.RecipeCategory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

private const val ARG_SELECTED_TAB_POSITION = "selectedTabPosition"

class RecipeTypesTabsFragment : Fragment(R.layout.fragment_recipe_types_tabs) {
    private var viewPager: ViewPager2? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.viewPager)
        viewPager!!.adapter = MyPagerAdapter(this)

        val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
        TabLayoutMediator(tabLayout, viewPager!!) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = RecipeCategory.Breakfast.toString()
                2 -> tab.text = RecipeCategory.Dinner.toString()
            }
        }.attach()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewPager?.setCurrentItem(MyPagerAdapter.selectedTabPosition ?: 0, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.let {
            MyPagerAdapter.selectedTabPosition = it.getInt(ARG_SELECTED_TAB_POSITION)
        }
    }

    class MyPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        companion object {
            var selectedTabPosition: Int? = null
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment.newInstance()
                1 -> RecipeRecycleViewFragment.newInstance(RecipeCategory.Breakfast)
                2 -> RecipeRecycleViewFragment.newInstance(RecipeCategory.Dinner)
                else -> HomeFragment.newInstance()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewPager?.let {
            outState.putInt(ARG_SELECTED_TAB_POSITION, it.currentItem)
        }

        super.onSaveInstanceState(outState)
    }
}
