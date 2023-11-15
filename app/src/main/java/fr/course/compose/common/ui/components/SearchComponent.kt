package fr.course.compose.common.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Search(text: String, onValueChange: (newValue: String) -> Unit) {
    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(1f)
    )
}

@Preview
@Composable
fun Search() {
    var text by rememberSaveable { mutableStateOf("") }

    TextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(1f)
    )
}