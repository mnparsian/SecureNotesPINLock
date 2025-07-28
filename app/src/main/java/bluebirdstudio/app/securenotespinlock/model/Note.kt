package bluebirdstudio.app.securenotespinlock.model

import androidx.compose.ui.text.style.TextAlign
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var title: String = "",
    var content: String = "", // حالا محتوای Markdown
    var category: String = "",
    var textColor: Long = 0xFF000000,
    var backgroundColor: Long = 0xFFFFFFFF,
    var fontSize: Int = 16,
    var date: String = "",
    var isBold: Boolean = false,
    val textAlign: String = "Start" // ذخیره به‌صورت String
)

