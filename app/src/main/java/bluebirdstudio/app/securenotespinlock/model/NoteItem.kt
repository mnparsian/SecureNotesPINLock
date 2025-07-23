package bluebirdstudio.app.securenotespinlock.model

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val previewText = note.content
        .lines()
        .take(2)
        .joinToString(" ")
        .let { if (it.length > 100) it.take(100) + "..." else it }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (note.title.isNotBlank()) {
                Text(
                    text = note.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                if (previewText != note.title) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = previewText,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        maxLines = 2
                    )
                }
            } else {
                Text(
                    text = previewText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3
                )
            }


            Spacer(modifier = Modifier.height(4.dp))

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }


    }
}
