package fr.course.compose

data class Course(val id: Int, var name: String)


fun getListe(): List<Course> {
    val list = mutableListOf<Course>()
    for(i: Int in 0..50) {
        list.add(Course(i, "Nom$i"))
    }
    return list
}
