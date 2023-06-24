package kz.mobydev.drevmass.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import kz.mobydev.drevmass.model.Lesson

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
}