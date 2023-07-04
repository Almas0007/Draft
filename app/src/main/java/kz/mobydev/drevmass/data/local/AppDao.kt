package kz.mobydev.drevmass.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kz.mobydev.drevmass.model.Lesson
import kz.mobydev.drevmass.model.Products

@Dao
interface AppDao {

    @Query("SELECT * FROM Lesson")
    suspend fun getLessonList(): List<Lesson>

    @Insert(onConflict = REPLACE)
    suspend fun setLessonList(lessonList: List<Lesson?>)

    @Query("SELECT * FROM Lesson WHERE id = :id")
    suspend fun getLessonById(id: Int): Lesson

    @Insert(onConflict = REPLACE)
    suspend fun setLesson(lesson: Lesson?)

    @Query("SELECT * FROM ProductsItem")
    suspend fun getProductsList(): List<Products.ProductsItem>

    @Insert(onConflict = REPLACE)
    suspend fun setProductsList(productsList: List<Products.ProductsItem>?)

    @Query("SELECT * FROM ProductsItem WHERE id = :id")
    suspend fun getProductsById(id: Int): Products.ProductsItem

    @Insert(onConflict = REPLACE)
    suspend fun setProduct(lesson: Products.ProductsItem?)
}