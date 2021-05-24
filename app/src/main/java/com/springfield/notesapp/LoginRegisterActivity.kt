package com.springfield.notesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginRegisterActivity : AppCompatActivity() {

    private val TAG = "LoginRegisterActivity"
    val AUTHUI_REQUEST_CODE = 10002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)

        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun handleLoginRegister(view: View) {
        val providers = arrayListOf<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.PhoneBuilder().build()
        )
        val intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
            .setLogo(R.drawable.ic_notes_logo)
            .setAlwaysShowSignInMethodScreen(true)
            .build()
        startActivityForResult(intent, AUTHUI_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // we have signed in the user or we have a new user
                val user = FirebaseAuth.getInstance().currentUser
                Log.d(TAG, "onActivityResult: " + user.email)
                if (user.metadata.creationTimestamp == user.metadata.lastSignInTimestamp) {
                    Toast.makeText(this, "Welcome new User", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                // signing in failed
                val response = IdpResponse.fromResultIntent(data)
                if (response == null) {
                    Log.d(TAG, "onActivityResult: the user has cancelled the sign in request")
                } else {
                    Log.e(TAG, "onActivityResult: ", response.error)
                }
            }
        }
    }
}