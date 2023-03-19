package com.example.cookingbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.ListFragment
import com.example.cookingbook.models.RecipeList

class RecipeListFragment : ListFragment() {

    companion object {
        interface Listener {
            fun itemClicked(id: Long)
        }
    }

    private var listener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val names = RecipeList.recipes.map { x -> x.name }.toList()
        listAdapter = ArrayAdapter(inflater.context, android.R.layout.simple_list_item_1, names)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as Listener
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)

        this.listener?.itemClicked(id)
    }
}