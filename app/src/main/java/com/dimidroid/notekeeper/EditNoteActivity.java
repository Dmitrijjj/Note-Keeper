package com.dimidroid.notekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextDescription;
    FloatingActionButton fab;
    String titleNew, descriptionNew, newData;
    int idOld;
    SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        connectUIComponents();

        showOldData();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleNew = editTextTitle.getText().toString();
                descriptionNew = editTextDescription.getText().toString();

                if (titleNew.isEmpty() || descriptionNew.isEmpty()){
                    Toast.makeText(getApplicationContext(),
                            "Please, add title and notes", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveData();
                }

            }
        });

    }

    private void saveData() {

        date = new Date();
        newData = formatter.format(date);

        Intent i = new Intent();
        i.putExtra("newTitle", titleNew);
        i.putExtra("newDescription", descriptionNew);
        i.putExtra("newDate", newData);

        if (idOld != -1){
            i.putExtra("newId", idOld);
            setResult(RESULT_OK, i);
            finish();
        }

    }

    private void showOldData() {

        Intent intent = getIntent();
        String titleOld = intent.getStringExtra("title");
        String descriptionOld = intent.getStringExtra("description");
        idOld = intent.getIntExtra("id", -1);
        String oldData = intent.getStringExtra("date");

        editTextTitle.setText(titleOld);
        editTextDescription.setText(descriptionOld);

    }

    private void connectUIComponents() {

        editTextTitle = findViewById(R.id.editTextTitleEdit);
        editTextDescription = findViewById(R.id.editTextNotesEdit);
        fab = findViewById(R.id.fabEdit);

    }
}