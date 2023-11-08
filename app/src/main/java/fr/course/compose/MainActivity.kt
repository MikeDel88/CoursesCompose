package fr.course.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.ui.theme.CoursesComposeTheme

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
                    MyApp(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier) {

    var list by rememberSaveable { mutableStateOf(getListe()) }
    var text by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(horizontal = 8.dp))  {

        Search(
            text,
            {
                text = it
                list = getListe()
                list = list.filter { course -> course.name.contains(text) }
            },
            modifier
        )
        CourseList(list = list, modifier = modifier)
    }

}

@Preview(showSystemUi = true)
@Composable
fun MyApp() {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Search()
        CourseList()
    }
}
