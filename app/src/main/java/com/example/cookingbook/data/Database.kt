package com.example.cookingbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cookingbook.R
import com.example.cookingbook.data.daos.RecipeDao
import com.example.cookingbook.data.entities.Recipe
import com.example.cookingbook.data.entities.RecipeCategory
import com.example.cookingbook.data.entities.RecipeIngredient
import com.example.cookingbook.data.entities.RecipeStep
import java.util.concurrent.Executors

@Database(entities = [Recipe::class, RecipeStep::class, RecipeIngredient::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        private fun seedDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    Executors.newSingleThreadExecutor().execute {
                        val recipeDao = getDatabase(context).recipeDao()

                        val recipes = listOf(
                            Recipe(
                                "Pancakes", "Easy", 30, 4, 300, RecipeCategory.Breakfast, R.drawable.pancakes
                            ),
                            Recipe(
                                "Pasta", "Easy", 30, 4, 300, RecipeCategory.Dinner, R.drawable.pasta
                            ),
                            Recipe(
                                "Pizza", "Easy", 30, 4, 300, RecipeCategory.Dinner, R.drawable.pizza
                            )
                        )

                        val ingredients = listOf(
                            listOf(
                                RecipeIngredient(1, "Flour", 1.0, "cup", ""),
                                RecipeIngredient(1, "Milk", 1.0, "cup", ""),
                                RecipeIngredient(1, "Eggs", 2.0, "", ""),
                                RecipeIngredient(1, "Sugar", 1.0, "tbsp", ""),
                                RecipeIngredient(1, "Salt", 1.0, "tsp", ""),
                                RecipeIngredient(1, "Baking powder", 1.0, "tsp", ""),
                                RecipeIngredient(1, "Butter", 1.0, "tbsp", "")
                            ),
                            listOf(
                                RecipeIngredient(2, "Pasta", 1.0, "cup", ""),
                                RecipeIngredient(2, "Tomato sauce", 1.0, "cup", ""),
                                RecipeIngredient(2, "Cheese", 1.0, "cup", ""),
                                RecipeIngredient(2, "Salt", 1.0, "tsp", ""),
                                RecipeIngredient(2, "Pepper", 1.0, "tsp", "")
                            ),
                            listOf(
                                RecipeIngredient(3, "Dough", 1.0, "cup", ""),
                                RecipeIngredient(3, "Tomato sauce", 1.0, "cup", ""),
                                RecipeIngredient(3, "Cheese", 1.0, "cup", ""),
                                RecipeIngredient(3, "Salt", 1.0, "tsp", ""),
                                RecipeIngredient(3, "Pepper", 1.0, "tsp", "")
                            )
                        )

                        val steps = listOf(
                            listOf(
                                RecipeStep(1, "Mix all ingredients", 0),
                                RecipeStep(1, "Cook on a pan", 1)
                            ),
                            listOf(
                                RecipeStep(2, "Cook pasta", 0),
                                RecipeStep(2, "Add tomato sauce", 1),
                                RecipeStep(2, "Add cheese", 2)
                            ),
                            listOf(
                                RecipeStep(3, "Cook dough", 0),
                                RecipeStep(3, "Add tomato sauce", 1),
                                RecipeStep(3, "Add cheese", 2)
                            )
                        )

                        recipes.forEachIndexed { index, recipe ->
                            val id = recipeDao.insert(recipe).toInt()

                            ingredients[index].forEach { ingredient ->
                                ingredient.recipeId = id
                            }

                            steps[index].forEach { step ->
                                step.recipeId = id
                            }

                            recipeDao.insert(steps[index], ingredients[index])
                        }
                    }
                }
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "recipes-app.db"
                ).addCallback(seedDatabaseCallback(context)).build()
                INSTANCE = instance

                instance
            }
        }
    }
}