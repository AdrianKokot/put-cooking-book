package com.example.cookingbook.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cookingbook.data.entities.*

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE category = :category")
    fun getAll(category: RecipeCategory): LiveData<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM recipe WHERE uid = :recipeId")
    fun getRecipe(recipeId: Int): LiveData<RecipeWithStepsAndIngredients>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe, steps: List<RecipeStep>, ingredients: List<RecipeIngredient>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(steps: List<RecipeStep>, ingredients: List<RecipeIngredient>)


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(recipe: Recipe): Long
}
