package fr.course.compose

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity
data class Courses(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var date: String = Date().formatCourse(),
    var icon: Int = getDrawable(name),
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdTime: String = "",
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val lastModifiedTime: String = ""
)

fun Date.formatCourse(): String {
    return try {
        val formatter = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())
        formatter.format(this)
    } catch (exception: Exception) {
        "Erreur date"
    }
}

fun getDrawable(name: String): Int {
    return when(name.lowercase().trim()) {
        "intermarche" -> R.drawable.icon_intermarche
        "carrefour" -> R.drawable.icon_carrefour
        "super u" -> R.drawable.icon_superu
        "leclerc" -> R.drawable.icon_leclerc
        else -> R.drawable.icon_inconnu
    }
}


