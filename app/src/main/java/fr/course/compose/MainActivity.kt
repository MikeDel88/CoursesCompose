package fr.course.compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import fr.course.compose.CourseLocaleDataSource.Companion.getListForTest
import fr.course.compose.ui.theme.CoursesComposeTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val courseViewModel: CourseViewModel by viewModels()
            val courseUiState by courseViewModel.uiState.collectAsState()

            CoursesComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScreenCourse(
                        uiCourseState = courseUiState,
                        findList = { name -> courseViewModel.findCourse(name) },
                        onClickItem = { item -> Toast.makeText(this, "Click sur item ${item.name}", Toast.LENGTH_SHORT).show() },
                        onRemoveItem =  { course -> courseViewModel.removeCourse(course) },
                        onAddItem = { course -> courseViewModel.addCourse(course) }
                    )
                }
            }
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

    Column(modifier = Modifier.padding(8.dp).fillMaxHeight())  {
        Search(
            text = text,
            onValueChange = { text = it; findList(it) }
        )
        CourseList(
            state = uiCourseState,
            onClickItem = onClickItem,
            onRemove = onRemoveItem,
            modifier = Modifier.weight(1f).fillMaxWidth()
        )
        FormCourse("", onAddItem)
    }
}

@Preview(heightDp = 900)
@Composable
fun ScreenCourse() {
    Column(modifier = Modifier.padding(8.dp).fillMaxHeight())  {
        Search("Intermarche") {}
        CourseList(UiCourseState(data = getListForTest()), {}, {}, Modifier.weight(1f).fillMaxWidth())
        FormCourse("Intermarche") {}
    }
}


