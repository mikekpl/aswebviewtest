package com.mikelau.aswebview.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.mikelau.aswebview.base.BaseViewModel
import com.mikelau.aswebview.utils.ValidateTokenUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class InAppWebViewViewModel(
    application: Application
) : BaseViewModel(application = application) {

    private val _isLoading = MutableStateFlow(true)
    val isLoading get() = _isLoading

    val showError = mutableStateOf(false)

    fun setErrorState(show: Boolean) {
        showError.value = show
    }

    fun initAsWebView(isAuthenticated: Boolean = false, jwt: String) {
        if (isAuthenticated) {
            viewModelScope.launch {
                val validateTokenCallback = object : ValidateTokenUtil.Callbacks {
                    override fun onSuccess() {
                        _isLoading.value = false
                        Log.e("COOKIE", "Validate JWT in In App WebView Success ")
                    }

                    override fun onFailure() {
                        _isLoading.value = false
                        Log.e("COOKIE", "Validate JWT in In App WebView Error ")
                    }
                }

                if (jwt.isNotBlank()) {
                    ValidateTokenUtil.validateJwt(
                        jwt = jwt,
                        value = VALIDATE_JWT_TOKEN_TARGET_URL_PATH,
                        callbacks = validateTokenCallback
                    )
                } else {
                    isLoading.value = false
                    Log.e("Jwt", "Error: Empty JWT value")
                }
            }
        } else {
            _isLoading.value = false
        }
    }

    companion object {
        private const val VALIDATE_JWT_TOKEN_TARGET_URL_PATH = "search"
    }
}