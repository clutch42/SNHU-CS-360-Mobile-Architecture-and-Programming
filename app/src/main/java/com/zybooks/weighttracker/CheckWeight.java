package com.zybooks.weighttracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CheckWeight {

    public static void showWeightAlert(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_check_weight, null);
        builder.setView(dialogView);

        String message = "Congratulations, you are at your target weight!";
        TextView dialogMessage = dialogView.findViewById(R.id.dialogMessage);
        Button dialogButton = dialogView.findViewById(R.id.dialogButton);
        dialogMessage.setText(message);

        AlertDialog alertDialog = builder.create();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (context instanceof Activity) {
                    ((Activity) context).finish();
                }
            }
        });
        sendSMS(context, message);
        alertDialog.show();
    }
    private static void sendSMS(Context context, String message) {
        // Replace "1234567890" with the actual phone number
        // can't really test this because the emulator doesn't have service
        // entering my personal number and debugging on my personal device
        // confirms that this works
        String phoneNumber = "1234567890";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "SMS sent to: " + phoneNumber, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}