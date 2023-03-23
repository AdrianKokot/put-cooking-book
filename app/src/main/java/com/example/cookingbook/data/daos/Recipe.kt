package com.example.cookingbook.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cookingbook.data.entities.Recipe
import com.example.cookingbook.data.entities.RecipeIngredient
import com.example.cookingbook.data.entities.RecipeStep
import com.example.cookingbook.data.entities.RecipeWithStepsAndIngredients

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipe")
    fun getAll(): LiveData<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM recipe WHERE uid = :recipeId")
    fun getRecipe(recipeId: Int): RecipeWithStepsAndIngredients

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
