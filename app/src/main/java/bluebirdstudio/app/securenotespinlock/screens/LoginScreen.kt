package bluebirdstudio.app.securenotespinlock.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var inputPin by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val invalidInputMsg = stringResource(R.string.error_invalid_input)
    val wrongPinMsg = stringResource(R.string.wrong_pin)

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
                modifier = Modifier.fillMaxWidth(0.85f) // کمی باریک‌تر
            ) {
                TextField(
                    value = inputPin,
                    onValueChange = { inputPin = it },
                    label = { Text(stringResource(R.string.enter_pin)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Button(
                    onClick = {
                        when {
                            inputPin.isBlank() -> {
                                scope.launch { snackbarHostState.showSnackbar(invalidInputMsg) }
                            }
                            !viewModel.verifyPin(inputPin) -> {
                                scope.launch { snackbarHostState.showSnackbar(wrongPinMsg) }
                            }
                            else -> {
                                navController.navigate(AppScreen.Notes.route) {
                                    popUpTo(AppScreen.Login.route) { inclusive = true }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.login))
                }

                TextButton(onClick = { navController.navigate(AppScreen.Reset.route) }) {
                    Text(stringResource(R.string.forgot_pin))
                }
            }
        }
    }
}
