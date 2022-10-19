package com.dimidroid.notekeeper.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dimidroid.notekeeper.MainActivity;
import com.dimidroid.notekeeper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class AddNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextDescription;
    FloatingActionButton fab;
    SimpleDateFormat formatter;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        connectUIComponents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

    }

    private void saveData() {

        formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");

        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        date = new Date();
        String stringDate = formatter.format(date);

        if (title.isEmpty() || description.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "PLease, add title and notes", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("description", description);
            intent.putExtra("date", stringDate);
            setResult(RESULT_OK, intent);
            finish();
            Toast.makeText(getApplicationContext(),
                    "New item was added", Toast.LENGTH_SHORT).show();
        }

    }

    private void connectUIComponents() {

        editTextTitle = findViewById(R.id.editTextTitleAdd);
        editTextDescription = findViewById(R.id.editTextNotesAdd);
        fab = findViewById(R.id.fabAdd);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}