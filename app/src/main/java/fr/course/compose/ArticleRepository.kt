package fr.course.compose

import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getListeOfArticle(): Flow<List<Articles>>
    suspend fun suppArticle(articles: Articles): Int
    suspend fun addArticle(articles: Articles): Long
    suspend fun getArticleById(id: Long): Flow<Articles>
    suspend fun updateArticle(articles: Articles): Int
}