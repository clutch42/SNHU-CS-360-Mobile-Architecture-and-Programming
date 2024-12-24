package com.zybooks.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DailyWeightAdapter extends RecyclerView.Adapter<DailyWeightAdapter.ViewHolder> {
    private Cursor dailyWeightCursor;
    private Cursor targetWeightCursor;
    private String username;
    private Context context;

    public DailyWeightAdapter(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    // ViewHolder class to hold references to the views
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDate;
        public TextView textViewCurrentWeight;
        public TextView textViewTargetWeight;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDateLabel);
            textViewCurrentWeight = itemView.findViewById(R.id.textViewCurrentWeightLabel);
            textViewTargetWeight = itemView.findViewById(R.id.textViewTargetWeightLabel);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);

            // Set OnClickListener for the edit button
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the data for the clicked item
                        if (dailyWeightCursor.moveToPosition(position)) {
                            long id = dailyWeightCursor.getLong(dailyWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID));
                            String date = dailyWeightCursor.getString(dailyWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_DATE));
                            double weight = dailyWeightCursor.getDouble(dailyWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_WEIGHT));

                            // Start the EditDataActivity, passing necessary data
                            Intent intent = new Intent(view.getContext(), EditData.class);
                            intent.putExtra("id", id);
                            intent.putExtra("username", username);
                            intent.putExtra("date", date);
                            intent.putExtra("weight", weight);
                            // Add more data as needed
                            view.getContext().startActivity(intent);
                        }
                    }
                }
            });
            // Set OnClickListener for the delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Get the data for the clicked item
                        if (dailyWeightCursor.moveToPosition(position)) {
                            long id = dailyWeightCursor.getLong(dailyWeightCursor.getColumnIndexOrThrow("id"));
                            DBHelper dbHelper = new DBHelper(view.getContext());
                            dbHelper.deleteDailyWeight(id);
                            Toast.makeText(itemView.getContext(), "Data Deleted", Toast.LENGTH_SHORT).show();
                            // Call a method to delete the data from the database
                            updateData(dbHelper);
                        }
                    }
                }
            });
        }
    }

    public void updateData(DBHelper dbHelper) {
        // Close the existing cursor to avoid memory leaks
        if (dailyWeightCursor != null && !dailyWeightCursor.isClosed()) {
            dailyWeightCursor.close();
        }
        if (targetWeightCursor != null && !targetWeightCursor.isClosed()) {
            targetWeightCursor.close();
        }
        // Update cursors with new data
        dailyWeightCursor = dbHelper.getAllDailyWeights(username);
        targetWeightCursor = dbHelper.getTargetWeight(username);

        // Notify the adapter that the data set has changed
        notifyDataSetChanged();

        // Debug logging
        if (targetWeightCursor != null) {
            Log.d("DailyWeightAdapter", "Target weight cursor count: " + targetWeightCursor.getCount());
            targetWeightCursor.moveToFirst();
            while (!targetWeightCursor.isAfterLast()) {
                String username = this.username;
                double targetWeight = targetWeightCursor.getDouble(targetWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_TARGET_WEIGHT));
                Log.d("DailyWeightAdapter", "Username: " + username + ", Target Weight: " + targetWeight);
                targetWeightCursor.moveToNext();
            }
        }
    }

    // Inflate the layout and create the ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (dailyWeightCursor.moveToPosition(position)) {
            String date = dailyWeightCursor.getString(dailyWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_DATE));
            double weight = dailyWeightCursor.getDouble(dailyWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_WEIGHT));

            // Log the data to check if it's correct
            Log.d("DailyWeightAdapter", "Date: " + date + ", Weight: " + weight);

            // Set the date and weight in your ViewHolder's TextViews
            holder.textViewDate.setText(date);
            holder.textViewCurrentWeight.setText(String.valueOf(weight));
            if (targetWeightCursor != null) {
                Log.d("DailyWeightAdapter", "Target weight cursor count: " + targetWeightCursor.getCount());
                while (targetWeightCursor.moveToNext()) {
                    String username = targetWeightCursor.getString(targetWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERNAME));
                    double targetWeight = targetWeightCursor.getDouble(targetWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_TARGET_WEIGHT));
                    Log.d("DailyWeightAdapter", "Username: " + username + ", Target Weight: " + targetWeight);
                }
            }
            // Fetch and display target weight if available
            if (targetWeightCursor != null && targetWeightCursor.moveToFirst()) {
                double targetWeight = targetWeightCursor.getDouble(targetWeightCursor.getColumnIndexOrThrow(DBHelper.COLUMN_TARGET_WEIGHT));
                holder.textViewTargetWeight.setText(String.valueOf(targetWeight));
            } else {
                // Use default value if target weight is not available
                holder.textViewTargetWeight.setText("Not entered");
            }
        }
    }

    @Override
    public int getItemCount() {
        return dailyWeightCursor.getCount();
    }
}