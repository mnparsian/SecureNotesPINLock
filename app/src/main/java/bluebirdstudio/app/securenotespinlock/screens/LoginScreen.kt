package bluebirdstudio.app.securenotespinlock.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import bluebirdstudio.app.securenotespinlock.R

@Composable
fun LoginScreen(
    context: Context,
    onLoginSuccess: () -> Unit,
    onForgotPin: () -> Unit
) {
    val prefs = context.getSharedPreferences("SecureNotesPrefs", Context.MODE_PRIVATE)
    val savedPin = prefs.getString("user_pin", null)
    var pin by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

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
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.enter_pin),
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = pin,
                    onValueChange = { if (it.length <= 4) pin = it },
                    label = { Text(stringResource(id = R.string.pin_label)) },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (message.isNotEmpty()) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                val wrongPinMessage = stringResource(R.string.wrong_pin)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (pin == savedPin) {
                                message = ""
                                onLoginSuccess()
                            } else {
                                message = wrongPinMessage
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.login))
                    }

                    OutlinedButton(
                        onClick = onForgotPin,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(id = R.string.forgot_pin))
                    }
                }
            }
        }
    }
}
