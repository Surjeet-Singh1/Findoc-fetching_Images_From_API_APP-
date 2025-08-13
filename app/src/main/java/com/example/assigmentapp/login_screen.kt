package com.example.assigmentapp // Make sure this matches your package name

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.regex.Pattern

// This is your new Login Activity
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        // Set the layout to your login screen XML file
        setContentView(R.layout.activity_login_screen)


        // Find the views from your layout file, including the TextInputLayouts for error messages
        val emailLayout = findViewById<TextInputLayout>(R.id.textInputLayout_email)
        val passwordLayout = findViewById<TextInputLayout>(R.id.textInputLayout_password)
        val emailEditText = findViewById<TextInputEditText>(R.id.editText_email)
        val passwordEditText = findViewById<TextInputEditText>(R.id.editText_password)
        val loginButton = findViewById<Button>(R.id.button_login)

        // Set the default text for easy testing with the new valid password
        emailEditText.setText("demo@email.com")
        passwordEditText.setText("Password123!")


        // Set a click listener on the login button
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            // Clear previous errors before validating again
            emailLayout.error = null
            passwordLayout.error = null

            // Validate email and password fields
            val isEmailValid = isValidEmail(email)
            val isPasswordValid = isValidPassword(password)

            // Show error on the email field if it's invalid
            if (!isEmailValid) {
                emailLayout.error = "Invalid email format"
            }

            // Show error on the password field if it's invalid
            if (!isPasswordValid) {
                passwordLayout.error = "Must be 8+ characters and include an uppercase, lowercase, number, & symbol."
            }

            // Only proceed if both fields are valid
            if (isEmailValid && isPasswordValid) {
                // --- This is the hardcoded login check ---
                // I've updated the password to one that passes validation
                if (email == "demo@email.com" && password == "Password123!") {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                    // Create an Intent to open the MainActivity (your home screen)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // Finish LoginActivity so the user can't go back to it
                    finish()
                } else {
                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Validates if the given email string is in a valid format.
     */
    private fun isValidEmail(email: String): Boolean {
        // An empty email is not valid
        if (email.isBlank()) return false
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Validates if the password meets the required criteria:
     * - Minimum 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one digit
     * - At least one special character (symbol)
     */
    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false

        val hasUpperCase = Pattern.compile("[A-Z]").matcher(password).find()
        val hasLowerCase = Pattern.compile("[a-z]").matcher(password).find()
        val hasDigit = Pattern.compile("[0-9]").matcher(password).find()
        val hasSymbol = Pattern.compile("[^A-Za-z0-9]").matcher(password).find()

        return hasUpperCase && hasLowerCase && hasDigit && hasSymbol
    }
}
