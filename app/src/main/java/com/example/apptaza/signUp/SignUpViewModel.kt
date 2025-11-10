package com.example.apptaza.signUp

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class SignUpViewModel(
    private val repo: SignUpRepository,
    private val savedState: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val TAG = "SignUpVM"
    }

    data class UiState(
        val phone: String = "",
        val isValid: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _state = MutableStateFlow(
        UiState(phone = savedState["phone"] ?: "")
    )
    val state: StateFlow<UiState> = _state

    private val _effects = Channel<Effect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    sealed interface Effect {
        data class GoToOtp(val phone: String) : Effect
    }

    init {
        Log.d(TAG, "init, restoredPhone=${mask(savedState["phone"] ?: "")}")
        logState("init")
    }

    fun onPhoneChanged(raw: String) {
        val digits = raw.filter(Char::isDigit)
        savedState["phone"] = digits
        val valid = validateRu7(digits)

        Log.d(TAG, "onPhoneChanged: raw='${raw.take(20)}' -> digits='${mask(digits)}', valid=$valid")

        _state.update {
            it.copy(phone = digits, isValid = valid, error = null)
        }
        logState("onPhoneChanged")
    }

    fun onContinue() {
        val phone = _state.value.phone
        if (!validateRu7(phone)) {
            Log.w(TAG, "onContinue: invalid phone='${mask(phone)}' -> ignore")
            return
        }
        if (_state.value.isLoading) {
            Log.w(TAG, "onContinue: already loading -> ignore")
            return
        }

        viewModelScope.launch {
            Log.i(TAG, "sendOtp START for phone='${mask(phone)}'")
            _state.update { it.copy(isLoading = true, error = null) }
            logState("onContinue:setLoading=true")

            val elapsed = measureTimeMillis {
                runCatching { repo.sendOtp(phone) }
                    .onSuccess {
                        Log.i(TAG, "sendOtp SUCCESS for phone='${mask(phone)}'")
                        _effects.send(Effect.GoToOtp(phone))
                    }
                    .onFailure { e ->
                        Log.e(TAG, "sendOtp FAILED: ${e.message}", e)
                        _state.update { s ->
                            s.copy(isLoading = false, error = e.message ?: "Ошибка")
                        }
                        logState("onContinue:failure")
                    }
            }
            Log.d(TAG, "sendOtp finished in ${elapsed}ms")
        }
    }

    private fun validateRu7(p: String): Boolean =
        p.length == 11 && p.startsWith("7")

    // --- helpers ---

    private fun mask(p: String): String =
        if (p.length <= 4) "*".repeat(p.length)
        else p.take(2) + "*".repeat(p.length - 4) + p.takeLast(2)

    private fun logState(origin: String) {
        val s = _state.value
        Log.d(
            TAG,
            "state@${origin}: phone='${mask(s.phone)}', isValid=${s.isValid}, isLoading=${s.isLoading}, error=${s.error}"
        )
    }
}
