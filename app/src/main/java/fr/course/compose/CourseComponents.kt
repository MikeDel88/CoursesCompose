package fr.course.compose

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

fun getListe(): List<Course> {
    val list = mutableListOf<Course>()
    for(i: Int in 0..50) {
        list.add(Course(i, "Nom$i"))
    }
    return list
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(modifier: Modifier) {
    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(1f)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Search() {
    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(1f)
    )
}

@Composable
fun CourseList(list: List<Course>, modifier: Modifier) {
    val context = LocalContext.current
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(list) { course ->
            CourseItem(course = course) {
                Toast.makeText(context, "Item ${course.id}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Preview(widthDp = 400)
@Composable
fun CourseList() {

    val context = LocalContext.current

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(getListe()) { course ->
            CourseItem(course = course) {
                Toast.makeText(context, "Item ${course.id}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseItem(course: Course, onClickItem: () -> Unit) {
    Card(onClick = onClickItem) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp),
            )
            Text(
                text = course.name,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(1f)

            )
        }
    }

}

@Preview(widthDp = 400)
@Composable
fun CourseItem() {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp, 4.dp)
                .fillMaxWidth(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp),
            )
            Text(
                text = "Bonjour",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(1f)
            )
        }
    }

}