package com.springfield.notesapp

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot

class NotesRecyclerAdapter(options: FirestoreRecyclerOptions<Note>, val noteListener: NoteListener) : FirestoreRecyclerAdapter<Note, NotesRecyclerAdapter.NoteViewHolder>(options) {


    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTextView = itemView.findViewById<TextView>(R.id.noteTextView)
        var dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)
        var checkBox = itemView.findViewById<CheckBox>(R.id.checkBox)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note_row, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int, model: Note) {
        holder.noteTextView.text = model.text
        holder.checkBox.isChecked = model.completed == true
        val dateformat = DateFormat.format("EEEE, MMM d, yyyy h:mm:ss a", model.timestamp?.toDate())
        holder.dateTextView.text = dateformat

        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val snapshot = snapshots.getSnapshot(position)
            val note = getItem(position)
            if (note.completed != isChecked) {
                noteListener.handleCheckChanged(isChecked, snapshot)
            }
        }
    }

    interface NoteListener {
        public fun handleCheckChanged(isChecked: Boolean, snapshot: DocumentSnapshot)
    }
}