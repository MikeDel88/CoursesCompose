package fr.course.compose.common.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import fr.course.compose.common.ui.theme.CoursesComposeTheme
import fr.course.compose.features.articles.ui.CourseDetailViewModel
import fr.course.compose.features.articles.ui.components.ScreenCourseDetail
import fr.course.compose.features.courses.ui.CourseViewModel
import fr.course.compose.features.courses.ui.components.ScreenCourse

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
                    // TODO: Voir pour un ActionFloatingButton pour ajouter une Course
                    // TODO: Voir pour afficher une BottonSheetBehavior lors du click pour afficher le formulaire
                    // TODO: Pareil pour l'ajout d'un article.
                    // TODO: Voir pour intégrer une Image de fond en plus gros avec la possibilité de modifier à la volée.
                    // TODO: Voir transition avec l'icone qui grossit jusqu'à ouvrir le détail.
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






