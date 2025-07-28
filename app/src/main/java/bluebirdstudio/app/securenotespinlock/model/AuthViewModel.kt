package bluebirdstudio.app.securenotespinlock.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    private val _pin = MutableStateFlow(prefs.getString("pin", "") ?: "")
    val pin: StateFlow<String> = _pin

    private val _securityQuestion = MutableStateFlow(prefs.getString("security_question", "") ?: "")
    val securityQuestion: StateFlow<String> = _securityQuestion

    private val _securityAnswer = MutableStateFlow(prefs.getString("security_answer", "") ?: "")
    val securityAnswer: StateFlow<String> = _securityAnswer

    fun register(pin: String, question: String, answer: String) {
        viewModelScope.launch {
            prefs.edit().apply {
                putString("pin", pin)
                putString("security_question", question)
                putString("security_answer", answer)
                apply()
            }
            _pin.value = pin
            _securityQuestion.value = question
            _securityAnswer.value = answer
        }
    }

    fun verifyPin(inputPin: String): Boolean {
        return _pin.value != null && _pin.value == inputPin
    }


    fun verifySecurityAnswer(input: String): Boolean {
        return input == _securityAnswer.value
    }

    fun resetPin(newPin: String) {
        viewModelScope.launch {
            prefs.edit().putString("pin", newPin).apply()
            _pin.value = newPin
        }
    }
}
