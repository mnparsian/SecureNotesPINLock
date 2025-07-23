package bluebirdstudio.app.securenotespinlock.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import bluebirdstudio.app.securenotespinlock.R
import bluebirdstudio.app.securenotespinlock.ui.theme.LanguageSelector

@Composable
fun RegistrationScreen(
    context: Context,
    onRegistered: () -> Unit
) {
    val prefs = context.getSharedPreferences("SecureNotesPrefs", Context.MODE_PRIVATE)
    var pin by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier

            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
//        // دکمه انتخاب زبان بالای صفحه
//        LanguageSelector(
//            modifier = Modifier.align(Alignment.Start)
//        )

        Spacer(Modifier.height(24.dp))

        // کارت ورودی ها
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.set_pin),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    label = { Text(stringResource(R.string.pin_label)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it },
                    label = { Text(stringResource(R.string.security_question_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text(stringResource(R.string.security_answer_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (message.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))
                val wrongInvalidInputMessage = stringResource(R.string.error_invalid_input)
                Button(
                    onClick = {
                        if (pin.length == 4 && question.isNotBlank() && answer.isNotBlank()) {
                            prefs.edit()
                                .putString("user_pin", pin)
                                .putString("security_question", question)
                                .putString("security_answer", answer.lowercase())
                                .apply()
                            onRegistered()
                        } else {
                            message = wrongInvalidInputMessage
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(text = stringResource(R.string.submit))
                }
            }
        }
    }
}
