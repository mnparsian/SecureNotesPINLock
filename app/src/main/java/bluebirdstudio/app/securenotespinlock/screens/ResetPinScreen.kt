package bluebirdstudio.app.securenotespinlock.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import bluebirdstudio.app.securenotespinlock.AppScreen
import bluebirdstudio.app.securenotespinlock.model.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import bluebirdstudio.app.securenotespinlock.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPinScreen(navController: NavController, viewModel: AuthViewModel) {
    val question by viewModel.securityQuestion.collectAsState()
    var answer by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val wrongAnswerMsg = stringResource(R.string.wrong_answer)
    val pinLengthErrorMsg = stringResource(R.string.pin_length_error)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Text(text = "${stringResource(R.string.question_prompt)} $question")

                TextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text(stringResource(R.string.security_answer_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                TextField(
                    value = newPin,
                    onValueChange = { newPin = it },
                    label = { Text(stringResource(R.string.new_pin)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        when {
                            !viewModel.verifySecurityAnswer(answer) -> {
                                scope.launch { snackbarHostState.showSnackbar(wrongAnswerMsg) }
                            }
                            newPin.length < 4 -> {
                                scope.launch { snackbarHostState.showSnackbar(pinLengthErrorMsg) }
                            }
                            else -> {
                                viewModel.resetPin(newPin)
                                navController.navigate(AppScreen.Login.route) {
                                    popUpTo(AppScreen.Reset.route) { inclusive = true }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
        }
    }
}
