package com.example.userinterface;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText, jurusanEditText;
    private Button submitButton;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.editTextTextPersonName2);
        jurusanEditText = findViewById(R.id.editTextTextPersonName4);
        submitButton = findViewById(R.id.button);

        databaseHelper = new DatabaseHelper(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String jurusan = jurusanEditText.getText().toString().trim();

                if (name.isEmpty() || jurusan.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean inserted = databaseHelper.insertFormData(name, jurusan);

                    if (inserted) {
                        Toast.makeText(MainActivity.this, "Form data inserted successfully", Toast.LENGTH_SHORT).show();
                        nameEditText.setText("");
                        jurusanEditText.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to insert form data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayFormData();
    }

    private void displayFormData() {
        Cursor cursor = databaseHelper.getAllFormData();
        StringBuilder data = new StringBuilder();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String jurusan = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_JURUSAN));

                data.append("Name: ").append(name).append("\n");
                data.append("Jurusan: ").append(jurusan).append("\n\n");
            } while (cursor.moveToNext());
            cursor.close();
        }

        TextView formDataTextView = findViewById(R.id.formDataTextView);
        formDataTextView.setText(data.toString());
    }

}
