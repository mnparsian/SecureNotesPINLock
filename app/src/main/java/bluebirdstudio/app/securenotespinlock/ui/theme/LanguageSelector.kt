package bluebirdstudio.app.securenotespinlock.ui.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.ui.unit.DpOffset
import bluebirdstudio.app.securenotespinlock.R
import java.util.*

@Composable
fun LanguageSelector(onLanguageChanged: () -> Unit) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = stringResource(R.string.select_language),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = DpOffset(x = 16.dp, y = 0.dp)

        ) {
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.english)) },
                onClick = {
                    setLocale(context, "en")
                    expanded = false
                    onLanguageChanged()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.persian)) },
                onClick = {
                    setLocale(context, "fa")
                    expanded = false
                    onLanguageChanged()
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(id = R.string.italian)) },
                onClick = {
                    setLocale(context, "it")
                    expanded = false
                    onLanguageChanged()
                }
            )
        }
    }
}

private fun setLocale(context: Context, languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val config = Configuration()
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
