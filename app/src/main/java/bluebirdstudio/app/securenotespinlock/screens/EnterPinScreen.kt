package bluebirdstudio.app.securenotespinlock.screens

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EnterPinScreen(
    context: Context,
    onSuccess: () -> Unit,
    onResetPin: () -> Unit
) {
    val prefs = context.getSharedPreferences("SecureNotesPrefs", Context.MODE_PRIVATE)

    // State for current saved PIN (from SharedPreferences)
    var savedPin by remember { mutableStateOf(prefs.getString("user_pin", null)) }

    // 🟢 Make isRegisterMode also State that depends on savedPin
    val isRegisterMode = savedPin == null

    // Other form states
    var pin by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (isRegisterMode) {
            // 🟢 Registration mode
            Text("ثبت PIN جدید و سوال امنیتی")

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { if (it.length <= 4) pin = it },
                label = { Text("PIN (4 رقم)") }
            )

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("سوال امنیتی شما") }
            )

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = answer,
                onValueChange = { answer = it },
                label = { Text("پاسخ به سوال امنیتی") }
            )

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                if (pin.length == 4 && question.isNotBlank() && answer.isNotBlank()) {
                    // Save to SharedPreferences
                    prefs.edit()
                        .putString("user_pin", pin)
                        .putString("security_question", question)
                        .putString("security_answer", answer)
                        .apply()

                    // 🟢 Update savedPin state so isRegisterMode becomes false!
                    savedPin = pin

                    error = ""
                    onSuccess()
                } else {
                    error = "لطفا همه فیلدها را درست پر کنید."
                }
            }) {
                Text("ثبت")
            }
        } else {
            // 🟢 Login mode
            Text("ورود با PIN")

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { if (it.length <= 4) pin = it },
                label = { Text("PIN") }
            )

            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                if (pin == savedPin) {
                    error = ""
                    onSuccess()
                } else {
                    error = "PIN اشتباه است."
                }
            }) {
                Text("ورود")
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = onResetPin) {
                Text("فراموشی / ریست PIN")
            }
        }

        Spacer(Modifier.height(16.dp))
        if (error.isNotEmpty()) {
            Text(error, color = MaterialTheme.colorScheme.error)
        }
    }
}
