package com.dimidroid.notekeeper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;

    LiveData<List<Note>> noteList;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        repository = new NoteRepository(application);
        noteList = repository.getAllNotes();

    }

    public void add(Note note){

        repository.add(note);

    }

    public void edit(Note note){

        repository.edit(note);

    }

    public void delete(Note note){

        repository.delete(note);

    }

    LiveData<List<Note>> getAllNotes(){

        return noteList;

    }

}
