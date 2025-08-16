import android.widget.Toast
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat

fun authenticateAndStopService(
    activity: androidx.fragment.app.FragmentActivity,
    stopServiceAction: () -> Unit
) {
    // Check if biometric authentication is available
    val biometricManager = androidx.biometric.BiometricManager.from(activity)
    when (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
        androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS -> {
            // Biometric features are available
        }
        androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Toast.makeText(activity, "No biometric features available on this device", Toast.LENGTH_SHORT).show()
            return
        }
        androidx.biometric.BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Toast.makeText(activity, "Biometric features are currently unavailable", Toast.LENGTH_SHORT).show()
            return
        }
        androidx.biometric.BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Toast.makeText(activity, "No biometric credentials enrolled", Toast.LENGTH_SHORT).show()
            return
        }
    }

    val executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = androidx.biometric.BiometricPrompt(
        activity,
        executor,
        object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // Handle user cancellation gracefully
                if (errorCode == androidx.biometric.BiometricPrompt.ERROR_USER_CANCELED ||
                    errorCode == androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    // User cancelled, don't show error toast
                    return
                }
                Toast.makeText(activity, "Authentication failed: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(activity, "Authentication successful", Toast.LENGTH_SHORT).show()
                stopServiceAction()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(activity, "Authentication failed. Try again", Toast.LENGTH_SHORT).show()
            }
        })

    val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
        .setTitle("Authenticate to stop service")
        .setSubtitle("Use fingerprint or device PIN")
        .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        .build()

    biometricPrompt.authenticate(promptInfo)
}