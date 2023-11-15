package fr.course.compose

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
import fr.course.compose.common.database.CourseAppDatabase
import fr.course.compose.features.articles.database.ArticleDao
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.courses.database.CourseDao
import fr.course.compose.features.courses.database.Courses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var courseDao: CourseDao
    private lateinit var articleDao: ArticleDao
    private lateinit var db: CourseAppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, CourseAppDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        courseDao = db.courseDao()
        articleDao = db.articleDao()

    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeCourse() = runBlocking {
        val idCourse = courseDao.insert(Courses(name = "Intermarche"))
        assertTrue(idCourse > 0)
    }

    @Test
    @Throws(Exception::class)
    fun writeCourseAndArticle() = runBlocking {
        val idCourse = courseDao.insert(Courses(name = "Intermarche"))
        assertTrue(idCourse > 0)

        val idArticle = articleDao.insert(Articles(courseId = idCourse, name = "Jambon"))
        assertTrue(idArticle > 0)
    }

    @Test
    @Throws(Exception::class)
    fun writeCourseAndArticleAndReadThem() = runBlocking {

        val idCourse = courseDao.insert(Courses(name = "Intermarche"))
        val idArticle = articleDao.insert(Articles(courseId = idCourse, name="Jambon"))

        val latch = CountDownLatch(1)
        val job = async(Dispatchers.IO) {
            courseDao.getCourseByIdWithArticles(idCourse).collect {

                assertEquals("Intermarche", it.courses?.name)
                assertEquals(idArticle, it.articles?.first()?.id)

                latch.countDown()
            }
        }
        withContext(Dispatchers.IO) {
            latch.await()
        }
        job.cancelAndJoin()
    }

}

