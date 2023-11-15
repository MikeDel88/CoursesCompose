package fr.course.compose.features.articles.datasource

import fr.course.compose.features.articles.database.ArticleDao
import fr.course.compose.features.articles.database.Articles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleLocalDataSource @Inject constructor(private val articleDao: ArticleDao) {

        companion object {
            fun getListForTest(): List<Articles> {
                return mutableListOf<Articles>().apply {
                   add(Articles(courseId = 1, name = "Article1", quantite = 1))
                   add(Articles(courseId = 1, name = "Article2", quantite = 1))
                   add(Articles(courseId = 1, name = "Article3", quantite = 1))
                   add(Articles(courseId = 1, name = "Article4", quantite = 1))
                   add(Articles(courseId = 1, name = "Article5", quantite = 1))
                   add(Articles(courseId = 1, name = "Article6", quantite = 1))
                }
            }

        }
        fun getListe(): Flow<List<Articles>> = articleDao.getAll()

        suspend fun deleteArticle(article: Articles) = withContext(Dispatchers.Default) {
            articleDao.delete(article)
        }

        suspend fun insertArticle(article: Articles) = withContext(Dispatchers.Default) {
            articleDao.insert(article)
        }

        suspend fun updateArticle(article: Articles) = withContext(Dispatchers.Default) {
            articleDao.update(article)
        }

        suspend fun getArticleById(id: Long) = withContext(Dispatchers.Default) {
            articleDao.getArticleById(id)
        }
}