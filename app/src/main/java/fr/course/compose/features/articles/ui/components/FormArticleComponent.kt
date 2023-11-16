package fr.course.compose.features.articles.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.course.compose.R
import fr.course.compose.features.articles.database.Articles

@Composable
fun FormArticle(id: Long, onClickValidate: (articles: Articles) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    var quantite by rememberSaveable { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                enabled = quantite > 1,
                onClick = { quantite-- },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = quantite.toString(),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                onClick = { quantite++ },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
        Button(
            onClick = {
                onClickValidate(Articles(courseId = id, name = text, quantite = quantite))
                text = ""
                quantite = 1 },
            enabled = text.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()) {
            Text(text= stringResource(R.string.bt_save).uppercase(), maxLines = 1)
        }
    }
}

@Preview
@Composable
fun FormArticle() {
    var text by rememberSaveable { mutableStateOf("") }
    var quantite by rememberSaveable { mutableIntStateOf(1) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                onClick = { if(quantite > 1) quantite-- },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = quantite.toString(),
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.size(8.dp))
            IconButton(
                modifier = Modifier.size(16.dp),
                onClick = { quantite++ },
                colors =  IconButtonDefaults.filledIconButtonColors()
            ) {
                Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
        Button(onClick = {}, modifier = Modifier.fillMaxWidth(), enabled = text.isNotEmpty()) {
            Text(text= stringResource(R.string.bt_save).uppercase(), maxLines = 1)
        }
    }
}