package fr.course.compose.features.articles.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import fr.course.compose.features.courses.database.Courses

@Entity(
    foreignKeys = [ForeignKey(
        entity = Courses::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("courseId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["courseId"])]
)
data class Articles(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val courseId: Long,
    var name: String,
    var quantite: Int = 1,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val createdTime: String = "",
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val lastModifiedTime: String = ""
)

class CourseWithDetail {

    @Embedded
    var courses: Courses? = null

    @Relation(entity = Articles::class,parentColumn = "id",entityColumn = "courseId")
    var articles: List<Articles>? = null
}