package bluebirdstudio.app.securenotespinlock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import bluebirdstudio.app.securenotespinlock.model.Note
import bluebirdstudio.app.securenotespinlock.data.Converters


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
