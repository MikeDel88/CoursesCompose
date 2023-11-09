package fr.course.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import fr.course.compose.CourseLocaleDataSource.Companion.getListForTest
import fr.course.compose.ui.theme.CoursesComposeTheme

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
    val context = LocalContext.current
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
            //TODO: Navigation to ScreenCourseDetail(Update Course and add or update or delete article)
        }
        composable("courses/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->

                val courseDetailViewModel: CourseDetailViewModel = hiltViewModel()
                val courseDetailState by courseDetailViewModel.uiState.collectAsState()
                ScreenCourseDetail(courseDetailState)
        }
    }
}

@Composable
fun ScreenCourseDetail(uiCourseDetailState: UiCourseDetailState) {
    Column {
        Text(text= uiCourseDetailState.data?.name ?: "")
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
        FormCourse("", onAddItem)
    }
}

@Preview(heightDp = 900)
@Composable
fun ScreenCourse() {
    Column(modifier = Modifier
        .padding(8.dp)
        .fillMaxHeight())  {
        Search("Intermarche") {}
        CourseList(UiCourseState(data = getListForTest()), {}, {},
            Modifier
                .weight(1f)
                .fillMaxWidth())
        FormCourse("Intermarche") {}
    }
}


