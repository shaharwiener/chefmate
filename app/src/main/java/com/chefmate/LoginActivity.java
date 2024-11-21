package com.chefmate;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

/**
 * LoginActivity manages user authentication through SMS verification.
 * Users input their phone number, receive an SMS with a verification code,
 * and enter the code to authenticate.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Request code for SMS permission.
     */
    private static final int SMS_PERMISSION_CODE = 100;

    /**
     * UI element for phone number input.
     */
    private EditText phoneInput;

    /**
     * TextView to display error messages for the verification code input.
     */
    private TextView codeError;

    /**
     * UI element for verification code input.
     */
    private EditText codeInput;

    /**
     * The generated 4-digit verification code sent via SMS.
     */
    private String generatedCode;

    /**
     * SharedPreferences file name for storing user identification state.
     */
    private static final String PREFS_NAME = "ChefMatePrefs";

    /**
     * Key in SharedPreferences for checking if the user is identified.
     */
    private static final String IDENTIFIED_KEY = "isUserIdentified";

    /**
     * Called when the activity is first created.
     * Initializes the UI, checks if the user is already authenticated, and sets up event handlers.
     *
     * @param savedInstanceState saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Check if the user is already identified
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isUserIdentified = sharedPreferences.getBoolean(IDENTIFIED_KEY, false);

        if (isUserIdentified) {
            navigateToHome();
            return;
        }

        // Initialize UI elements
        phoneInput = findViewById(R.id.phoneInput);
        codeError = findViewById(R.id.codeError);
        codeInput = findViewById(R.id.codeInput);

        // Set initial visibility
        findViewById(R.id.codeView).setVisibility(View.GONE);
        codeError.setVisibility(View.GONE);

        // Handle phone number submission
        Button phoneButton = findViewById(R.id.phoneButton);
        phoneButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
            } else {
                sendVerificationCode();
            }
        });

        // Handle code verification
        Button codeButton = findViewById(R.id.codeButton);
        codeButton.setOnClickListener(v -> verifyCode());
    }

    /**
     * Sends a verification code to the entered phone number via SMS.
     * Displays an error message if the phone number is invalid.
     */
    private void sendVerificationCode() {
        String phoneNumber = phoneInput.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 10) {
            Toast.makeText(this, "הכנס מספר טלפון תקין", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a random 4-digit code
        generatedCode = String.format("%04d", new Random().nextInt(10000));

        // Send SMS using SmsManager
        try {
            StringBuilder smsStringBuilder = new StringBuilder();
            smsStringBuilder.append("ברוך הבא ל ChefMate !!!").append("\n").append("קוד האימות שלך הוא: ").append(generatedCode);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, smsStringBuilder.toString(), null, null);
            Toast.makeText(this, "קוד האימות נשלח בהצלחה!", Toast.LENGTH_SHORT).show();

            // Show the verification code input field
            findViewById(R.id.codeView).setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Toast.makeText(this, "שגיאה בשליחת SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Verifies the entered code against the generated code.
     * Displays an error message if the codes do not match.
     * Marks the user as identified if the code matches and navigates to the HomeActivity.
     */
    private void verifyCode() {
        String inputCode = codeInput.getText().toString().trim();

        if (TextUtils.isEmpty(inputCode)) {
            codeError.setVisibility(View.VISIBLE);
            codeError.setText("נא להזין קוד אימות");
            return;
        }

        if (!inputCode.equals(generatedCode)) {
            codeError.setVisibility(View.VISIBLE);
            codeError.setText("קוד שגוי, נסה שוב");
            return;
        }

        // If the code matches, save user identification and navigate to HomeActivity
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IDENTIFIED_KEY, true).apply();

        Toast.makeText(this, "אימות הצליח!", Toast.LENGTH_SHORT).show();
        navigateToHome();
    }

    /**
     * Navigates the user to the HomeActivity.
     */
    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Handles the result of the SMS permission request.
     * If permission is granted, proceeds to send the verification code.
     * If permission is denied, displays a toast message.
     *
     * @param requestCode  the request code of the permission
     * @param permissions  the requested permissions
     * @param grantResults the grant results for the requested permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendVerificationCode();
            } else {
                Toast.makeText(this, "לא ניתן לשלוח SMS ללא הרשאה", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
