package com.rafdi.vitechasia.blog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.User;
import com.rafdi.vitechasia.blog.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout emailLayout, passwordLayout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        
        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            navigateToMain();
            return;
        }

        // Set up login button click listener
        findViewById(R.id.loginButton).setOnClickListener(v -> attemptLogin());
        
        // Set up signup button click listener
        findViewById(R.id.signupButton).setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });
    }

    private void attemptLogin() {
        // Reset errors
        emailLayout.setError(null);
        passwordLayout.setError(null);

        // Get values
        String email = emailLayout.getEditText().getText().toString().trim();
        String password = passwordLayout.getEditText().getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.invalid_password));
            focusView = passwordLayout;
            cancel = true;
        } else if (password.length() < 6) {
            passwordLayout.setError(getString(R.string.invalid_password));
            focusView = passwordLayout;
            cancel = true;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.invalid_email));
            focusView = emailLayout;
            cancel = true;
        } else if (!isValidEmail(email)) {
            emailLayout.setError(getString(R.string.invalid_email));
            focusView = emailLayout;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first form field with an error
            focusView.requestFocus();
        } else {
            // For demo purposes, we'll use dummy data
            // In a real app, you would validate credentials with your backend
            if (email.equals("demo@example.com") && password.equals("password123")) {
                // Create a dummy user (replace with actual user data from your backend)
                User user = new User("1", "Demo User", email, "");
                // Save the user session
                sessionManager.createLoginSession(user);
                // Navigate to main activity
                navigateToMain();
            } else {
                // Show error message
                Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
