package com.pp.chatproject

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider.getCredential
import com.pp.chatproject.model.Person
import com.pp.chatproject.service.AuthService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mGoogleApiClient: GoogleApiClient? = null
    private val RC_SIGN_IN = 9001
    //private var mStatusTextView: TextView? = null
   // private var mDetailTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_activity_sign_in_button.setOnClickListener(this)
        main_activity_sign_out_button.setOnClickListener(this)
        main_activity_disconnect_button.setOnClickListener(this)



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // [END config_signin]

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        if(user != null) {
            updateUI(user)
            AuthService.getInstance().updateUser(user, {person: Person? ->
                if(person != null) goToDashboard()
            })
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(mAuth.currentUser)

    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        // Firebase sign out
        mAuth.signOut()

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { updateUI(null) }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                if(account != null) {
                    firebaseAuthWithGoogle(account)
                }
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        //showProgressDialog()

        val credential = getCredential(acct.idToken, null)

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                       val user = mAuth.currentUser
                        if(user != null) {
                            updateUI(user)
                            AuthService.getInstance().updateUser(user, {person: Person? ->
                                if(person != null) goToDashboard()
                            })
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                    }

                   // hideProgressDialog()
                }
    }

    private fun revokeAccess() {
        // Firebase sign out
        mAuth.signOut()

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                object : ResultCallback<Status> {
                    override fun onResult(status: Status) {
                        updateUI(null)
                    }
                })
    }


    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(v: View) {

        var i: Int? = v.getId()
        if (i == R.id.main_activity_sign_in_button) {
            signIn()
        } else if (i == R.id.main_activity_sign_out_button) {
            signOut()
        } else if (i == R.id.main_activity_disconnect_button) {
            revokeAccess()
        }

    }

    private fun updateUI(user: FirebaseUser?) {
       // hideProgressDialog()
        if (user != null) {


            main_activity_status.setText(getString(R.string.google_status_fmt, user.email))
            main_activity_detail.setText(getString(R.string.firebase_status_fmt, user.uid))

            findViewById<View>(R.id.main_activity_sign_in_button).visibility = View.GONE
            findViewById<View>(R.id.main_activity_sign_out_and_disconnect).visibility = View.VISIBLE


        } else {
            AuthService.getInstance().logout()
            main_activity_status.setText(R.string.signed_out)
            main_activity_detail.setText(null)

            findViewById<View>(R.id.main_activity_sign_in_button).visibility = View.VISIBLE
            findViewById<View>(R.id.main_activity_sign_out_and_disconnect).visibility = View.GONE
        }
    }

    public fun goToDashboard(){
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}
