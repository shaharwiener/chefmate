package com.chefmate;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * ProfileActivity allows users to update their profile image by either capturing a new photo using
 * the device's camera or selecting an existing image from the gallery.
 */
public class ProfileActivity extends BaseActivity {

    private static final int PERMISSION_REQUEST = 1003;

    private ImageView profileImage;
    private Uri imageUri;

    // Activity result launchers
    private ActivityResultLauncher<Intent> cameraLauncher;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.profile_activity, "פרופיל משתמש");
        showPageLayout(false);

        profileImage = findViewById(R.id.profileImage);
        Button profileImageUploader = findViewById(R.id.profile_image_upload_button);

        // Initialize activity result launchers
        initializeActivityResultLaunchers();

        // Set up the button to show the image picker dialog
        profileImageUploader.setOnClickListener(v -> showImagePickerDialog());

        showPageLayout(true);
    }

    /**
     * Initializes the activity result launchers for camera and gallery actions.
     */
    private void initializeActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        profileImage.setImageURI(imageUri); // Set image from camera
                    }
                });

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        profileImage.setImageURI(selectedImageUri); // Set image from gallery
                    }
                });
    }

    /**
     * Displays a dialog for the user to choose between taking a photo using the camera
     * or selecting an image from the gallery.
     */
    private void showImagePickerDialog() {
        String[] options = {"השתמש במצלמה", "בחר מהגלריה"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("בחר מקור התמונה");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Camera option
                if (checkPermissions()) {
                    openCamera();
                }
            } else if (which == 1) {
                // Gallery option
                openGallery();
            }
        });
        builder.show();
    }

    /**
     * Checks if the required permissions (camera and storage) are granted.
     * If not, requests the permissions.
     *
     * @return true if permissions are already granted, false otherwise
     */
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST);
                return false;
            }
        }
        return true;
    }

    /**
     * Opens the device's camera app to capture a photo.
     * Saves the photo to a file.
     */
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                imageUri = FileProvider.getUriForFile(this, "com.chefmate.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                cameraLauncher.launch(cameraIntent);
            } catch (IOException e) {
                showToast("Failed to open camera: " + e.getMessage());
            }
        } else {
            showToast("No camera app available.");
        }
    }

    /**
     * Opens the device's gallery for the user to select an image.
     */
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    /**
     * Handles the result of permission requests.
     * Opens the camera if permissions are granted; otherwise, shows a toast message.
     *
     * @param requestCode  the request code
     * @param permissions  the requested permissions
     * @param grantResults the results for the requested permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                showToast("Permissions denied!");
            }
        }
    }

    /**
     * Creates a temporary file to store the captured image.
     *
     * @return the created image file
     * @throws IOException if an error occurs during file creation
     */
    private File createImageFile() throws IOException {
        String fileName = "profile_image";
        File storageDir = getExternalFilesDir(null);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    /**
     * Displays a toast message.
     *
     * @param message the message to display
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
