package fr.course.compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import fr.course.compose.ui.theme.CoursesComposeTheme

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
                    )
                }
            }
        }
    }
}
