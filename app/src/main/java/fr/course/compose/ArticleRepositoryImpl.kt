package fr.course.compose

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(private val articleLocalDataSource: ArticleLocalDataSource): ArticleRepository {
    override fun getListeOfArticle(): Flow<List<Articles>> = articleLocalDataSource.getListe()
    override suspend fun suppArticle(articles: Articles): Int = articleLocalDataSource.deleteArticle(articles)
    override suspend fun addArticle(articles: Articles): Long = articleLocalDataSource.insertArticle(articles)
    override suspend fun getArticleById(id: Long): Flow<Articles> = articleLocalDataSource.getArticleById(id)
    override suspend fun updateArticle(articles: Articles): Int = articleLocalDataSource.updateArticle(articles)
}