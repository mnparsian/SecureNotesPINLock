package bluebirdstudio.app.securenotespinlock

sealed class AppScreen(val route: String) {
    object Registration : AppScreen("registration")
    object Login : AppScreen("login")
    object Notes : AppScreen("notes")
    object Reset : AppScreen("reset")

    data class AddEditNote(val noteId: Int? = null) : AppScreen("add_edit_note/${noteId ?: -1}")
}
