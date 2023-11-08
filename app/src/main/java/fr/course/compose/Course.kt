package fr.course.compose

import android.content.Context
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.text.SimpleDateFormat
import java.util.Date
import kotlinx.serialization.json.*

@Serializable
data class Course(val id: Int, var name: String, var date: String, val icon: Int)

fun Date.formatCourse(): String {
    return try {
        //TODO: voir pour formattage correcte selon pays.
        val formatter = SimpleDateFormat("EEEE dd MMMM Y")
        formatter.format(this)
    } catch (exception: Exception) {
        "Erreur date"
    }

}

fun getListForTest(): List<Course> {
    return mutableListOf<Course>().apply {
        add(Course(0, "Intermarche", Date().formatCourse(),R.drawable.icon_intermarche))
        add(Course(1, "Leclerc", Date().formatCourse(),R.drawable.icon_leclerc))
        add(Course(2, "Super U", Date().formatCourse(),R.drawable.icon_superu))
        add(Course(3, "Carrefour", Date().formatCourse(),R.drawable.icon_carrefour))
        add(Course(4, "Casino", Date().formatCourse(),R.drawable.icon_inconnu))
    }
}

fun getListe(): List<Course> {
    val sharedPreference = App.appContext.getSharedPreferences("liste", Context.MODE_PRIVATE)
    val jsonListeOfCourses = sharedPreference.getString("courses", "")
    return Json.decodeFromString(jsonListeOfCourses ?: "")
}

fun saveListOfCourse(list: List<Course>) {
    val sharedPreference = App.appContext.getSharedPreferences("liste", Context.MODE_PRIVATE)
    val listeString = Json.encodeToString(list)
    sharedPreference.edit().putString("courses", listeString).apply()
}


