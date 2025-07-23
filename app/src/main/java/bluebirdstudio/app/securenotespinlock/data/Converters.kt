package bluebirdstudio.app.securenotespinlock.data

import androidx.room.TypeConverter
import androidx.compose.ui.text.style.TextAlign

class Converters {

    @TypeConverter
    fun fromTextAlign(textAlign: TextAlign): String {
        return when (textAlign) {
            TextAlign.Left -> "Left"
            TextAlign.Right -> "Right"
            TextAlign.Center -> "Center"
            TextAlign.Start -> "Start"
            TextAlign.End -> "End"
            else -> "Start"
        }
    }

    @TypeConverter
    fun toTextAlign(value: String): TextAlign {
        return when (value) {
            "Left" -> TextAlign.Left
            "Right" -> TextAlign.Right
            "Center" -> TextAlign.Center
            "Start" -> TextAlign.Start
            "End" -> TextAlign.End
            else -> TextAlign.Start
        }
    }
}
