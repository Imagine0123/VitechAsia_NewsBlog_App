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

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout nameLayout, emailLayout, passwordLayout, confirmPasswordLayout;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);
        
        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Set up signup button click listener
        findViewById(R.id.signupButton).setOnClickListener(v -> attemptSignup());
        
        // Set up login button click listener
        findViewById(R.id.loginButton).setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void attemptSignup() {
        // Reset errors
        nameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        // Get values
        String name = nameLayout.getEditText().getText().toString().trim();
        String email = emailLayout.getEditText().getText().toString().trim();
        String password = passwordLayout.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordLayout.getEditText().getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Validate name
        if (TextUtils.isEmpty(name)) {
            nameLayout.setError(getString(R.string.invalid_name));
            focusView = nameLayout;
            cancel = true;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError(getString(R.string.invalid_email));
            focusView = focusView == null ? emailLayout : focusView;
            cancel = true;
        } else if (!isValidEmail(email)) {
            emailLayout.setError(getString(R.string.invalid_email));
            focusView = focusView == null ? emailLayout : focusView;
            cancel = true;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError(getString(R.string.invalid_password));
            focusView = focusView == null ? passwordLayout : focusView;
            cancel = true;
        } else if (password.length() < 6) {
            passwordLayout.setError(getString(R.string.invalid_password));
            focusView = focusView == null ? passwordLayout : focusView;
            cancel = true;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordLayout.setError(getString(R.string.password_mismatch));
            focusView = focusView == null ? confirmPasswordLayout : focusView;
            cancel = true;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError(getString(R.string.password_mismatch));
            focusView = focusView == null ? confirmPasswordLayout : focusView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first form field with an error
            focusView.requestFocus();
        } else {
            // For demo purposes, we'll just create a session with the provided details
            // In a real app, you would send these details to your backend
            try {
                // Create a dummy user (in a real app, this would come from your backend)
                User user = new User(
                    String.valueOf(System.currentTimeMillis()), // Generate a unique ID
                    name,
                    email,
                    "" // No profile photo URL for now
                );
                
                // Save the user session
                sessionManager.createLoginSession(user);
                
                // Show success message
                Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                
                // Navigate to main activity
                navigateToMain();
            } catch (Exception e) {
                // Show error message
                Toast.makeText(this, R.string.signup_error, Toast.LENGTH_SHORT).show();
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
