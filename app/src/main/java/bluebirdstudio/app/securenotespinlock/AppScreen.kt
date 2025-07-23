package bluebirdstudio.app.securenotespinlock

import bluebirdstudio.app.securenotespinlock.model.Note



sealed class AppScreen {
    object Registration : AppScreen()
    object Login : AppScreen()
    object Notes : AppScreen()
    object Reset : AppScreen()
    data class AddNote(val note: Note? = null) : AppScreen()
}
