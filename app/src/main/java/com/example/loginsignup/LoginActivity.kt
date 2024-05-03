package com.example.loginsignup

import android.app.Activity
import android.content.ContentProviderClient
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginsignup.R.string.default_web_client
import com.example.loginsignup.databinding.ActivityLoginBinding

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
private  lateinit var auth:FirebaseAuth
private lateinit var googleSignInClient: GoogleSignInClient
    val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth= FirebaseAuth.getInstance()

        binding.btnsignin.setOnClickListener {
            val email=binding.edtusername.text.toString()
            val password=binding.edtpassword.text.toString()


            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please enter all details", Toast.LENGTH_LONG).show()

            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){

                        if(it.isSuccessful){
                            Toast.makeText(this,"Sign-In Sucessfull",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this,"User Login failed: ${it.exception?.message}",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        binding.btnsignup.setOnClickListener{

            startActivity(Intent(this,SignUpActivity::class.java))
        }


        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client))
            .requestEmail().build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)

        val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->

            if(result.resultCode==Activity.RESULT_OK){
                val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)

                if(task.isSuccessful){
                    val account:GoogleSignInAccount?=task.result
                    val credential=GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful){
                            Toast.makeText(this,"Signed In",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        }else{
                            Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                        }

                    }

                }else{
                    Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                }


            }else{
                Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
            }
        }

        binding.google.setOnClickListener {
            val signInClient=googleSignInClient.signInIntent

            launcher.launch(signInClient)



        }




    }



    override fun onStart() {
        super.onStart()

        val currentuser=auth.currentUser
        if(currentuser!=null){
           startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}