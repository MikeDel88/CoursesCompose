package fr.course.compose.features.courses.ui.components

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        BasicTextField(
            value = text,
            modifier = Modifier.weight(1f),
            onValueChange = { newText ->
                text = newText
            },
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.inversePrimary,
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .weight(1f)
                        .padding(all = 8.dp), // inner padding
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    innerTextField()
                    IconButton(onClick = { openDatePicker = true }, modifier = Modifier.padding(0.dp)) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = Color.DarkGray
                        )
                    }
                }
            },
        )
        Spacer(Modifier.size(8.dp))
        IconButton(
            enabled = text.isNotEmpty(),
            onClick = {
                courses.name = text
                courses.date = dateSelected
                courses.icon = getDrawable(text)

                onClickValidate(courses)
            }
        ) {
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

    Row(verticalAlignment = Alignment.CenterVertically,  modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
        BasicTextField(
            value = text,
            modifier = Modifier.weight(1f),
            onValueChange = { newText ->
                text = newText
            },
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            ),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.inversePrimary,
                            shape = RoundedCornerShape(size = 16.dp)
                        )
                        .weight(1f)
                        .padding(all = 8.dp), // inner padding
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    innerTextField()
                    IconButton(onClick = { openDatePicker = true }, modifier = Modifier.padding(0.dp)) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Date",
                            tint = Color.DarkGray
                        )
                    }
                }
            },
        )
        Spacer(Modifier.size(8.dp))
        IconButton(
            enabled = text.isNotEmpty(),
            onClick = {}
        ) {
            Icon(Icons.Default.Done, contentDescription = stringResource(id = R.string.bt_valider))
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            SimpleDatePickerInDatePickerDialog(openDatePicker, { openDatePicker = false }) {
                    date -> dateSelected = if(date != null) Date(date).formatCourse() else ""
            }
    }


}