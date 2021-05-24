package com.springfield.notesapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener, NotesRecyclerAdapter.NoteListener  {

    private val TAG = "MainActivity"

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesRecyclerAdapter: NotesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            showAlertDialog()
        }



//        if (FirebaseAuth.getInstance().currentUser == null) {
//            startLoginActivity()
//        } else {
//            FirebaseAuth.getInstance().currentUser.getIdToken(true)
//                .addOnSuccessListener {
//                    Log.d(TAG, "onSuccess: " + it.token)
//                }
//        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginRegisterActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAlertDialog() {
        val noteEditText = EditText(this)
        noteEditText.setPadding(30, 0, 0, 30)
        AlertDialog.Builder(this)
                .setTitle("Add Note")
                .setView(noteEditText)
                .setPositiveButton("Add") { _: DialogInterface, i: Int ->
                    Log.d(TAG, "onClick: ${noteEditText.text}")
                    addNote(noteEditText.text.toString())
                }
                .setNegativeButton("Cancel", null)
                .show()
    }

    private fun addNote(text: String) {
        val userId = FirebaseAuth.getInstance().currentUser.uid
        val note = Note(text, false, Timestamp(Date()), userId)

        FirebaseFirestore.getInstance()
                .collection("notes")
                .add(note)
                .addOnSuccessListener {
                    Log.d(TAG, "onSuccess: ${note.text}")
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_profile -> {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_logout ->  {
                Toast.makeText(this, "Logged out...", Toast.LENGTH_SHORT).show()
                AuthUI.getInstance().signOut(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
        if (notesRecyclerAdapter != null) {
            notesRecyclerAdapter.stopListening()
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser == null) {
            startLoginActivity()
            return
        }

        firebaseAuth.currentUser.getIdToken(true)
            .addOnSuccessListener {
                Log.d(TAG, "onSuccess: " + it.token)
            }

        Log.d(TAG, "onAuthStateChanged: " + firebaseAuth.currentUser.phoneNumber)

        initRecyclerView(firebaseAuth.currentUser)
    }

    private fun initRecyclerView(user: FirebaseUser) {

        val query = FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("userId", user.uid)

        val options = FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note::class.java)
                .build()

        notesRecyclerAdapter = NotesRecyclerAdapter(options, this)
        recyclerView.adapter = notesRecyclerAdapter
        notesRecyclerAdapter.startListening()

    }

    override fun handleCheckChanged(isChecked: Boolean, snapshot: DocumentSnapshot) {
        Log.d(TAG, "handleCheckChanged: $isChecked")
        snapshot.reference.update("completed", isChecked)
                .addOnSuccessListener {
                    Log.d(TAG, "onSuccess ")
                }
                .addOnFailureListener {

                }
    }


}