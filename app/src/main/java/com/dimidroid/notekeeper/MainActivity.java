package com.dimidroid.notekeeper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    SearchView searchView;
    RecyclerView recyclerView;
    NoteViewModel noteViewModel;
    NoteAdapter adapter;
    ActivityResultLauncher<Intent> activityResultLauncherForAddNote;
    //List<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectUIComponents();

        connectAdapter();

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> noteList) {

                //Todo: update RecyclerView here
                adapter.setNotes(noteList);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNoteActivity();
            }
        });

    }

    private void goToAddNoteActivity() {

        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        activityResultLauncherForAddNote.launch(intent);

    }

    private void connectAdapter() {

        adapter = new NoteAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    private void connectUIComponents() {

        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);

    }

    public void registerActivityForAddNote(){
        activityResultLauncherForAddNote = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        int resultCode = result.getResultCode();
                        Intent data = result.getData();

                        if (resultCode == RESULT_OK && data != null){
                            String title = data.getStringExtra("title");
                            String description = data.getStringExtra("description");
                            String date = data.getStringExtra("date");
                            Note note = new Note(title, description, date);
                            noteViewModel.add(note);

                        }

                    }
                });
    }
}