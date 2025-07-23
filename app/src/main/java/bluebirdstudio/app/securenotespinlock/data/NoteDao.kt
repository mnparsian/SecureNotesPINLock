package bluebirdstudio.app.securenotespinlock.data

import androidx.room.*
import bluebirdstudio.app.securenotespinlock.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY id DESC")
    suspend fun getAllNotes(): List<Note>

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}