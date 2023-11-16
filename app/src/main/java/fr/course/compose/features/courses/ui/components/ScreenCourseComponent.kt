package fr.course.compose.features.courses.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.common.ui.components.Search
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.ui.UiCourseState

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