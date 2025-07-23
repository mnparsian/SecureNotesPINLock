package bluebirdstudio.app.securenotespinlock.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import bluebirdstudio.app.securenotespinlock.R

@Composable
fun ResetPinScreen(
    context: Context,
    onResetSuccess: () -> Unit
) {
    val prefs = context.getSharedPreferences("SecureNotesPrefs", Context.MODE_PRIVATE)
    val question = prefs.getString("security_question", stringResource(R.string.no_security_question))
    val savedAnswer = prefs.getString("security_answer", "") ?: ""

    var answer by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var step by remember { mutableStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .widthIn(max = 400.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (step == 1) {
                    Text(
                        text = stringResource(R.string.security_answer_label),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = question ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text(stringResource(R.string.security_answer_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    val wrongAnswerMessage = stringResource(R.string.wrong_answer)
                    Button(
                        onClick = {
                            if (answer.trim().lowercase() == savedAnswer.lowercase()) {
                                step = 2
                                message = ""
                            } else {
                                message = wrongAnswerMessage
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.confirm_answer))
                    }
                } else {
                    Text(
                        text = stringResource(R.string.set_new_pin),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newPin,
                        onValueChange = { if (it.length <= 4) newPin = it },
                        label = { Text(stringResource(R.string.pin_label)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (message.isNotEmpty()) {
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    val wrongPINLengthMessage = stringResource(R.string.pin_length_error)
                    Button(
                        onClick = {
                            if (newPin.length == 4) {
                                prefs.edit().putString("user_pin", newPin).apply()
                                onResetSuccess()
                            } else {
                                message = wrongPINLengthMessage
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.submit))
                    }
                }
            }
        }
    }
}
