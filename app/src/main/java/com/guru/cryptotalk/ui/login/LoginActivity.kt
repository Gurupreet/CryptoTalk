package com.guru.cryptotalk.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.guru.cryptotalk.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.guru.cryptotalk.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    companion object {
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private const val RC_SIGN_IN = 100;
    }
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupSignup()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        sign_in_button.setOnClickListener { signIn() }
    }

    private fun setupSignup() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("527906989717-emrnbbgl9tmsinqfeapusf427auohie6.apps.googleusercontent.com")
            .requestEmail()
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Snackbar.make(main, "Google Sign in Successful"+account?.email, Snackbar.LENGTH_SHORT).show()
                    // firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Snackbar.make(main, "Google Sign in failed", Snackbar.LENGTH_SHORT).show()
                // Google Sign In failed, update UI appropriately
                Log.w("", "Google sign in failed", e)
                // ...
            }
        }
    }



    private fun checkPlayServices(): Boolean {
        val googleAPI = GoogleApiAvailability.getInstance()
        val result = googleAPI.isGooglePlayServicesAvailable(this)
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                val dialog = googleAPI.getErrorDialog(
                    this, result,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )
                dialog.setCancelable(false)
                dialog.show()
            }
            return false
        }
        return true
    }

}
