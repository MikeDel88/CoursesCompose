package fr.course.compose.common.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import fr.course.compose.R
import fr.course.compose.common.ui.components.Loading
import fr.course.compose.common.ui.components.Search
import fr.course.compose.common.ui.theme.CoursesComposeTheme
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.datasource.ArticleLocalDataSource
import fr.course.compose.features.articles.ui.components.ArticleItem
import fr.course.compose.features.articles.ui.components.ArticleList
import fr.course.compose.features.articles.ui.components.FormArticle
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.ui.CourseDetailViewModel
import fr.course.compose.features.courses.ui.CourseViewModel
import fr.course.compose.features.courses.ui.UiCourseDetailState
import fr.course.compose.features.courses.ui.UiCourseState
import fr.course.compose.features.courses.ui.components.CourseList
import fr.course.compose.features.courses.ui.components.FormCourse

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            CoursesComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable(route = "home") {
            val courseViewModel: CourseViewModel =  hiltViewModel()
            val courseUiState by courseViewModel.uiState.collectAsState()

            ScreenCourse(
                uiCourseState = courseUiState,
                findList = { name -> courseViewModel.findCourse(name) },
                onClickItem = { course -> navController.navigate("courses/${course.id}")},
                onRemoveItem =  { course -> courseViewModel.removeCourse(course) },
                onAddItem = { course -> courseViewModel.addCourse(course) }
            )
        }
        composable(route = "courses/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType }),
            enterTransition = { slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            ) },
            exitTransition = {slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            ) },
            popEnterTransition = { slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            ) },
            popExitTransition = {  slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(700)
            ) }
        ) { backStackEntry ->

                val courseDetailViewModel: CourseDetailViewModel = hiltViewModel()
                val courseDetailState by courseDetailViewModel.uiState.collectAsState()
                ScreenCourseDetail(
                    uiCourseDetailState = courseDetailState,
                    onUpdateItem = { course -> courseDetailViewModel.majCourse(course)},
                    onAddArticleItem = { article -> courseDetailViewModel.addArticle(article) },
                    onDeleteArticleItem = { article -> courseDetailViewModel.deleteArticle(article) },
                    onChangeQuantiteArticleItem = { article -> courseDetailViewModel.updateArticle(article) }
                )
        }
    }
}

@Composable
fun ScreenCourseDetail(
    uiCourseDetailState: UiCourseDetailState,
    onUpdateItem: (course: Courses) -> Unit,
    onAddArticleItem: (article: Articles) -> Unit,
    onDeleteArticleItem: (article: Articles) -> Unit,
    onChangeQuantiteArticleItem: (article: Articles) -> Unit
) {
    if(uiCourseDetailState.loading ) {
        Loading(stringResource(R.string.load_generic), modifier = Modifier)
    } else if(uiCourseDetailState.data?.courses == null) {
        Text(text= stringResource(R.string.load_data_not_found))
    } else {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()) {
            FormCourse(
                courses = uiCourseDetailState.data.courses!!,
                onClickValidate = onUpdateItem
            )
            Divider()
            if(uiCourseDetailState.data.articles.isNullOrEmpty()) {
                Text(text = stringResource(R.string.article_not_found), modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(), textAlign = TextAlign.Center)
            } else {
                ArticleList(
                    state = uiCourseDetailState,
                    onQuantiteChange = onChangeQuantiteArticleItem,
                    onDeleteArticle = onDeleteArticleItem,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
            FormArticle(
                id = uiCourseDetailState.data.courses!!.id,
                onClickValidate = onAddArticleItem)
        }
    }

}

@Preview(heightDp = 900)
@Composable
fun ScreenCourseDetail() {
    val loading by remember {
        mutableStateOf(false)
    }
    val course by remember {
        mutableStateOf(Courses(name="Intermarche"))
    }
    val data by remember {
        mutableStateOf(ArticleLocalDataSource.getListForTest())
    }
    if(loading) {
        Loading(stringResource(R.string.load_generic), modifier = Modifier)
    } else if(course == null) {
        Text(text=stringResource(R.string.load_data_not_found), modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    } else {
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxHeight()) {
            FormCourse(
                courses = course,
                onClickValidate = {}
            )
            Divider()
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(data) { article ->
                    ArticleItem(article, {}, {})
                }
            }
            FormArticle(
                id = course.id,
                onClickValidate = {})
        }
    }

}

@Composable
fun ScreenCourse(
    uiCourseState: UiCourseState,
    findList: (text: String) -> Unit,
    onClickItem: (item: Courses) -> Unit,
    onRemoveItem : (course: Courses) -> Unit,
    onAddItem : (course: Courses) -> Unit
)
{
    var text by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxHeight())  {
        Search(
            text = text,
            onValueChange = { text = it; findList(it) }
        )
        CourseList(
            state = uiCourseState,
            onClickItem = onClickItem,
            onRemove = onRemoveItem,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        FormCourse(Courses(name=""), onAddItem)
    }
}

@Preview(heightDp = 900)
@Composable
fun ScreenCourse() {
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxHeight())  {
        Search("Intermarche") {}
        CourseList(
            UiCourseState(loading = true), {}, {},
            Modifier
                .weight(1f)
                .fillMaxWidth())
        FormCourse(Courses(name = "Intermarche")) {}
    }
}


