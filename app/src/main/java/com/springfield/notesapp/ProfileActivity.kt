package com.springfield.notesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var circleImageView: CircleImageView
    private lateinit var displayNameEditText: TextInputEditText
    private lateinit var updateProfileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        circleImageView = findViewById(R.id.circleImageView)
        displayNameEditText = findViewById(R.id.displayNameEditText)
        updateProfileButton = findViewById(R.id.updateProfileButton)
    }

    fun updateProfile(view: View) {

    }
}