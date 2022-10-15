package com.dimidroid.notekeeper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    List<Note> noteList = new ArrayList<>();

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note currentNote = noteList.get(position);

        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewDate.setText(currentNote.getDate());

        holder.cardView.setCardBackgroundColor(holder.itemView.getResources()
                .getColor(myRandomColor(), null));

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void setNotes(List<Note> notes){
        this.noteList = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position){
        return noteList.get(position);
    }

    public void filterList(List<Note> filteredList){
        this.noteList = filteredList;
        notifyDataSetChanged();
    }

    public int myRandomColor(){

        Random random = new Random();
        List<Integer> colorArray = new ArrayList<>();
        colorArray.add(R.color.green);
        colorArray.add(R.color.blue);
        colorArray.add(R.color.yellow);
        colorArray.add(R.color.red);
        int color = random.nextInt(colorArray.size());

        return colorArray.get(color);
        }

    public class NoteViewHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle, textViewDescription, textViewDate;
        CardView cardView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDesc);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            cardView = itemView.findViewById(R.id.cardView);

        }

    }
}


