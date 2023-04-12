package com.example.cookingbook

import android.content.Context
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookingbook.data.AppDatabase
import com.example.cookingbook.data.entities.Recipe
import com.example.cookingbook.data.entities.RecipeCategory

private const val ARG_CATEGORY = "category"

class RecipeRecycleViewFragment : Fragment(R.layout.fragment_recipe_recycle_view) {
    private var category: RecipeCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            it.getString(ARG_CATEGORY)?.let { category ->
                this.category = RecipeCategory.valueOf(category)
            }
        }
    }

    private var listener: RecipeListFragment.Companion.Listener? = null

    class MyViewHolder(view: View, private val listener: RecipeListFragment.Companion.Listener?) :
        RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.imageView)
        private val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        private val cardView = view.findViewById<CardView>(R.id.cardView)

        fun bind(item: Recipe) {
            imageView.setImageResource(item.imageId)
            nameTextView.text = item.name
            cardView.setOnClickListener {
                this.listener?.itemClicked(item.uid)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = this.category ?: return
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = MyAdapter(emptyList(), this.listener)
        d("RecipeRecycleViewFragment", "onViewCreated: $category")

        AppDatabase.getDatabase(requireContext())
            .recipeDao()
            .getAll(category)
            .observe(viewLifecycleOwner) { items ->
                recyclerView.adapter = MyAdapter(items, this.listener)
            }
    }

    class MyAdapter(private val items: List<Recipe>, private val listener: RecipeListFragment.Companion.Listener?) :
        RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
            return MyViewHolder(view, this.listener)
        }

        override fun getItemCount(): Int = items.size
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.listener = context as RecipeListFragment.Companion.Listener
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ARG_CATEGORY, this.category?.name)
        super.onSaveInstanceState(outState)
    }

    companion object {
        @JvmStatic
        fun newInstance(category: RecipeCategory) = RecipeRecycleViewFragment().apply {
            this.category = category
        }
    }
}