package fr.course.compose.features.courses.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.common.ui.components.SimpleDatePickerInDatePickerDialog
import fr.course.compose.features.courses.database.Courses
import fr.course.compose.features.courses.database.formatCourse
import fr.course.compose.features.courses.database.getDrawable
import java.util.Date

@Composable
fun FormCourse(courses: Courses, modifier: Modifier, onClickValidate: (courses: Courses) -> Unit) {

    var text by rememberSaveable { mutableStateOf(courses.name) }
    var dateSelected by rememberSaveable { mutableStateOf(courses.date) }
    var openDatePicker by rememberSaveable { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        TextField(
            trailingIcon = {
                IconButton(
                    onClick = { openDatePicker = true },
                    colors =  IconButtonDefaults.filledIconButtonColors()
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.size(8.dp))
        Button( onClick = {
            courses.name = text
            courses.date = dateSelected
            courses.icon = getDrawable(text)

            onClickValidate(courses)
        }) {
            Icon(Icons.Default.Done, contentDescription = stringResource(id = R.string.bt_valider))
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && openDatePicker)
            SimpleDatePickerInDatePickerDialog(true, { openDatePicker = false }) {
                    date -> dateSelected = if(date != null) Date(date).formatCourse() else Date().formatCourse()
            }
    }

}

@Preview
@Composable
fun FormCourse() {
    var text by rememberSaveable { mutableStateOf("") }
    var dateSelected by rememberSaveable { mutableStateOf(Date().formatCourse()) }
    var openDatePicker by rememberSaveable { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(
            trailingIcon = {
                IconButton(
                    onClick = { openDatePicker = true },
                    colors =  IconButtonDefaults.filledIconButtonColors()
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.size(8.dp))
        Button(onClick = {}) {
            Icon(Icons.Default.Done, contentDescription = stringResource(id = R.string.bt_valider))
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            SimpleDatePickerInDatePickerDialog(openDatePicker, { openDatePicker = false }) {
                    date -> dateSelected = if(date != null) Date(date).formatCourse() else ""
            }
    }


}