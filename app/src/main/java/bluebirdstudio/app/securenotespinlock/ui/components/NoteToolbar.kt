package bluebirdstudio.app.securenotespinlock.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class ToolbarAction(
    val icon: ImageVector,
    val description: String,
    val onClick: () -> Unit
)

@Composable
fun NoteToolbar(
    actions: List<ToolbarAction>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        actions.forEach { action ->
            IconButton(onClick = action.onClick) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.description
                )
            }
        }
    }
}
