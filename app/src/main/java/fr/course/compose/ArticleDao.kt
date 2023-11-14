package fr.course.compose

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles ORDER BY articles.name")
    fun getAll(): Flow<List<Articles>>

    @Query("SELECT * FROM articles WHERE id = :articleId")
    fun getArticleById(articleId: Long): Flow<Articles>

    @Insert(entity = Articles::class)
    suspend fun insert(articles: Articles) : Long

    @Delete(entity = Articles::class)
    fun delete(articles: Articles) : Int

    @Update(entity = Articles::class)
    fun update(articles: Articles) : Int
}
