package com.dimidroid.notekeeper;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dimidroid.notekeeper.Activities.AddNoteActivity;
import com.dimidroid.notekeeper.Activities.EditNoteActivity;
import com.dimidroid.notekeeper.Adapter.NoteAdapter;
import com.dimidroid.notekeeper.Model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    RecyclerView recyclerView;
    NoteViewModel noteViewModel;
    NoteAdapter adapter;
    ActivityResultLauncher<Intent> activityResultLauncherForAddNote;
    ActivityResultLauncher<Intent> activityResultLauncherForEditNote;
    List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectUIComponents();

        connectAdapter();

        registerActivityForAddNote();

        registerForEditActivity();

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

        adapter.onItemClick(new NoteAdapter.ItemClickListenerI() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("description", note.getDescription());
                intent.putExtra("date", note.getDate());
                activityResultLauncherForEditNote.launch(intent);
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

    public void registerForEditActivity(){

        activityResultLauncherForEditNote = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Intent data = result.getData();
                        int resultCode = result.getResultCode();

                        if (resultCode == RESULT_OK && data != null){
                            String newTitle = data.getStringExtra("newTitle");
                            String newDescription = data.getStringExtra("newDescription");
                            int id = data.getIntExtra("newId", -1);
                            String date = data.getStringExtra("newDate");
                            Note newNote = new Note(newTitle, newDescription, date);
                            newNote.setId(id);
                            noteViewModel.edit(newNote);
                        }

                    }
                });
    }
}