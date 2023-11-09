package fr.course.compose

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface CourseDao {

    @Query("SELECT * FROM courses ORDER BY courses.createdTime")
    fun getAll(): Flow<List<Courses>>

    @Query("SELECT * FROM courses WHERE id = :coursesId")
    fun getCourseById(coursesId: Int): Flow<Courses>

    @Query("SELECT * FROM courses WHERE name LIKE :name")
    fun getCourseByName(name: String): Flow<List<Courses>>

    @Insert
    suspend fun insert(courses: Courses)

    @Delete
    fun delete(courses: Courses)

    @Update
    fun update(courses: Courses)
}