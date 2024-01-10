package fr.course.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.course.compose.common.ui.*
import fr.course.compose.common.ui.theme.CoursesComposeTheme
import fr.course.compose.features.articles.database.Articles
import fr.course.compose.features.articles.ui.CourseDetailViewModel
import fr.course.compose.features.articles.ui.UiCourseDetailState
import fr.course.compose.features.articles.ui.components.ScreenCourseDetail
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.ui.CourseViewModel
import fr.course.compose.features.courses.ui.UiCourseState
import fr.course.compose.features.courses.ui.components.ScreenCourse


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoursesComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSizeClass = calculateWindowSizeClass(activity = this)
                    MyApp(windowSizeClass)
                }
            }
        }
    }
}

@Composable
fun MyApp(
    windowSizeClass: WindowSizeClass
) {
    val typeOfNavigation = when(windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> TYPE_NAVIGATION.LIST
        else -> TYPE_NAVIGATION.LIST_DETAIL
    }

    val navController = rememberNavController()
    NavHost(navController, startDestination = Course.route) {
        composable(route = Course.route) {
            val courseViewModel: CourseViewModel = hiltViewModel()
            val courseUiState by courseViewModel.uiState.collectAsState()

            ScreenCourse(
                uiCourseState = courseUiState,
                findList = { name -> courseViewModel.findCourse(name) },
                onClickItem = { course -> navController.navigate("courses/${course.id}") },
                onRemoveItem =  { course -> courseViewModel.removeCourse(course) },
                onAddItem = { course -> courseViewModel.addCourse(course) },
                onRefreshList = { courseViewModel.refreshList() }
            )
        }
        composable(route = Article.routeWithArgs,
            arguments = Article.arguments,
            enterTransition = { slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(700)
            ) },
            exitTransition = { slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
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
                    onChangeQuantiteArticleItem = { article -> courseDetailViewModel.updateArticle(article) },
                    onFilterChange = { filter -> courseDetailViewModel.filterListe(filter) }
                )
        }
    }
}

@Composable
fun ScreenCourseAndDetail(modifier: Modifier = Modifier, stateCourse: UiCourseState, stateDetail: UiCourseDetailState) {
    Row(modifier = modifier) {
        ScreenCourse(
            modifier = Modifier.weight(0.3f),
            uiCourseState = stateCourse,
            findList = {},
            onClickItem = {},
            onRemoveItem =  {},
            onAddItem = {},
            onRefreshList = {}
        )
        ScreenCourseDetail(
            modifier = Modifier.weight(0.7f),
            uiCourseDetailState = UiCourseDetailState(data = stateDetail.data, loading = stateDetail.loading),
            onUpdateItem = {},
            onAddArticleItem = {},
            onDeleteArticleItem = {},
            onChangeQuantiteArticleItem = {},
            onFilterChange = {}
        )
    }
}

@Preview(widthDp = 840)
@Preview(widthDp = 1200)
@Composable
fun ScreenCourseAndDetailPreview() {
    val courses = Courses(1, "Intermarche")
    val articles = listOf<Articles>()
    CoursesComposeTheme {
        ScreenCourseAndDetail(
            modifier = Modifier,
            stateCourse = UiCourseState(listOf(), false),
            stateDetail = UiCourseDetailState(data = Pair(courses, articles), loading = false)
        )
    }
}






