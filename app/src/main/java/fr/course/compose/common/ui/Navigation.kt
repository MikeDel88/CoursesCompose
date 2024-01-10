package fr.course.compose.common.ui

import androidx.navigation.NavType
import androidx.navigation.navArgument

abstract class Navigation {
    abstract val route: String
}

object Course: Navigation() {
    override val route: String = "home"
}

object Article: Navigation() {
    override val route: String = "courses"
    val routeWithArgs: String = "courses/{id}"
    val arguments = listOf(navArgument("id") { type = NavType.LongType })
}

enum class TYPE_NAVIGATION {
    LIST, LIST_DETAIL
}