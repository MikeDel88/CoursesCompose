package fr.course.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SimpleDatePickerInDatePickerDialog(
    openDialog: Boolean,
    onDismiss: () -> Unit,
    onValueChange: (date: Long?) -> Unit,
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )
    if(openDialog)
    {
        DatePickerDialog(
            shape = RoundedCornerShape(6.dp),
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(onClick = { onValueChange(datePickerState.selectedDateMillis); onDismiss() }
                ) {
                    Text(text="OK")
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
                dateValidator = { timestamp ->
                    timestamp > Instant.now().toEpochMilli()
                }
            )
        }
    }
}