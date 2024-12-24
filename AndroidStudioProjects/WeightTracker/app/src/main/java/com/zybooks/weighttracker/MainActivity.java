package com.zybooks.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button createAccountButton;
    private Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        createAccountButton = findViewById(R.id.buttonCreateAccount);
        loginButton = findViewById(R.id.buttonLogin);

        dbHelper = new DBHelper(this);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                usernameEditText.setText("");
                passwordEditText.setText("");
                if (!username.isEmpty() && !password.isEmpty()) {
                    insertUserData(username, password);
                    // Optionally, show a message or navigate to another screen
                } else {
                    // Handle empty fields
                    Toast.makeText(MainActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                usernameEditText.setText("");
                passwordEditText.setText("");
                if (!username.isEmpty() && !password.isEmpty()) {
                    if (checkCredentials(username, password)) {
                        // Login successful, you can navigate to another screen or perform other actions
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Login successful, navigate to MainScreen activity with username
                        Intent mainScreenIntent = new Intent(MainActivity.this, MainScreen.class);
                        mainScreenIntent.putExtra("USERNAME", username);
                        startActivity(mainScreenIntent);
                    } else {
                        // Login failed, show an error message
                        Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                    // Optionally, show a message or navigate to another screen
                } else {
                    // Handle empty fields
                    Toast.makeText(MainActivity.this, "Username and password are required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to check if the entered username and password match the database
    private boolean checkCredentials(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DBHelper.TABLE_NAME_USERS,
                new String[]{DBHelper.COLUMN_PASSWORD},
                DBHelper.COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    String storedPassword = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PASSWORD));
                    // Check if the entered password matches the stored password
                    return password.equals(storedPassword);
                }
            } finally {
                cursor.close();
            }
        }

        return false;
    }
    // Method to insert data into the database
    private void insertUserData(String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if the username already exists
        if (!isUsernameExists(db, username)) {
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_USERNAME, username);
            values.put(DBHelper.COLUMN_PASSWORD, password);
            try {
                db.insert(DBHelper.TABLE_NAME_USERS, null, values);
                Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        } else {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
        }
    }
    // Helper method to check if a username already exists in the database
    private boolean isUsernameExists(SQLiteDatabase db, String username) {
        Cursor cursor = db.query(
                DBHelper.TABLE_NAME_USERS,
                new String[]{DBHelper.COLUMN_USERNAME},
                DBHelper.COLUMN_USERNAME + "=?",
                new String[]{username},
                null,
                null,
                null
        );

        if (cursor != null) {
            try {
                return cursor.getCount() > 0;
            } finally {
                cursor.close();
            }
        }

        return false;
    }
}