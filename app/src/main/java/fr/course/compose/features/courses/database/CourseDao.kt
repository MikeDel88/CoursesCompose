package fr.course.compose.features.courses.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.course.compose.features.articles.database.CourseWithDetail
import kotlinx.coroutines.flow.Flow


@Dao
interface CourseDao {

    @Query("SELECT * FROM courses ORDER BY courses.date DESC")
    fun getAll(): Flow<List<Courses>>

    @Query("SELECT * FROM courses WHERE id = :coursesId")
    fun getCourseById(coursesId: Long): Flow<Courses>

    @Insert(entity = Courses::class)
    suspend fun insert(courses: Courses) : Long

    @Delete(entity = Courses::class)
    fun delete(courses: Courses) : Int

    @Update(entity = Courses::class)
    fun update(courses: Courses) : Int

    @Query("SELECT * FROM courses WHERE id = :id")
    fun getCourseByIdWithArticles(id: Long): Flow<CourseWithDetail>
}