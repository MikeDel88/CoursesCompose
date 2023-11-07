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
                    MyApp(modifier = Modifier
                        .padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun MyApp(modifier: Modifier) {
    val list = getListe()
    Column(modifier = Modifier.padding(horizontal = 8.dp))  {
        if(list.size > 1)
        {
            Search(modifier)
        }
        CourseList(list = getListe(), modifier = modifier)
    }

}

@Preview(showSystemUi = true)
@Composable
fun MyApp() {
    val list = getListe()
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        if(list.size > 1)
        {
            Search()
        }
        CourseList()
    }
}
