package com.dimidroid.notekeeper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    SearchView searchView;
    RecyclerView recyclerView;
    NoteViewModel noteViewModel;
    NoteAdapter adapter;
    ActivityResultLauncher<Intent> activityResultLauncherForAddNote;
    List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectUIComponents();

        connectAdapter();

        registerActivityForAddNote();

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> noteList) {

                //Todo: update RecyclerView here
                adapter.setNotes(noteList);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddNoteActivity();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT
                | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
             @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAbsoluteAdapterPosition()));
                Toast.makeText(MainActivity.this,
                        "Item was deleted!", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);

    }

    private void filter(String newText) {

        List<Note> filteredList = new ArrayList<>();

        for (Note singleNote : noteList){

            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getDescription().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getDate().toLowerCase().contains(newText.toLowerCase())){

                filteredList.add(singleNote);

            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(MainActivity.this, "Notes not found", Toast.LENGTH_SHORT).show();
        }
        else {
            adapter.filterList(filteredList);
        }
    }

    private void goToAddNoteActivity() {

        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        activityResultLauncherForAddNote.launch(intent);

    }

    private void connectAdapter() {

        adapter = new NoteAdapter();
        noteList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    private void connectUIComponents() {

        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
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