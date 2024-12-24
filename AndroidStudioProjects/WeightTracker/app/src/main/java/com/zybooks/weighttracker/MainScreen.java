package com.zybooks.weighttracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainScreen extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_SMS = 1;
    private RecyclerView recyclerView;
    private DailyWeightAdapter adapter;
    private DBHelper dbHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_display);

        // Retrieve the username from the intent
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");

        // Initialize DBHelper
        dbHelper = new DBHelper(this);

        // Create a TextView and set its text
        TextView greetingTextView = findViewById(R.id.textViewGreeting);
        greetingTextView.setText("Hello, " + username);

        // Create the DailyWeightAdapter with both cursors
        adapter = new DailyWeightAdapter(this, username);


        recyclerView = findViewById(R.id.recyclerViewData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        Button buttonAddData = findViewById(R.id.buttonAddData);
        buttonAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AddWeight activity with username
                Intent addWeightIntent = new Intent(MainScreen.this, AddWeight.class);
                addWeightIntent.putExtra("USERNAME", username);
                startActivity(addWeightIntent);
            }
        });

        Button buttonChangeTarget = findViewById(R.id.buttonChangeTargetWeight);
        buttonChangeTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to AddWeight activity with username
                Intent changeTargetIntent = new Intent(MainScreen.this, ChangeTargetWeight.class);
                changeTargetIntent.putExtra("USERNAME", username);
                startActivity(changeTargetIntent);
            }
        });

        Button buttonRequestPermission = findViewById(R.id.buttonRequestPermission);
        TextView textViewPermissionStatus = findViewById(R.id.textViewSMS);

        buttonRequestPermission.setOnClickListener(v -> requestSmsPermission());

        // Check permission status and update the text view accordingly
        if (checkSmsPermission()) {
            textViewPermissionStatus.setText("SMS Permission Status: Granted");
            // Perform actions for granted permissions, such as sending SMS notifications
        } else {
            textViewPermissionStatus.setText("SMS Permission Status: Denied");
        }
    }

    private void updateRecyclerView() {
        // Update the dataset in the adapter
        adapter.updateData(dbHelper);

        // Notify the adapter that the dataset has changed
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.updateData(dbHelper);

    }


    private void requestSmsPermission() {
        // Request SMS permissions
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.SEND_SMS},
                PERMISSION_REQUEST_SMS
        );
    }

    private boolean checkSmsPermission() {
        // Check if SMS permissions are granted
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Handle permission request result
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                TextView textViewPermissionStatus = findViewById(R.id.textViewSMS);
                textViewPermissionStatus.setText("SMS Permission Status: Granted");
                // Perform actions for granted permissions, such as sending SMS notifications
            } else {
                // Permission denied
                TextView textViewPermissionStatus = findViewById(R.id.textViewSMS);
                textViewPermissionStatus.setText("SMS Permission Status: Denied");
            }
        }
    }
}