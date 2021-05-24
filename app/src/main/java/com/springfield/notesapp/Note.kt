package com.springfield.notesapp

import com.google.firebase.Timestamp

data class Note(
        val text: String? = null,
        val completed: Boolean? = null,
        val timestamp: Timestamp? = null,
        val userId: String? = null
) {
}