package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginsignup.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private  lateinit var auth: FirebaseAuth
    val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
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

        auth=FirebaseAuth.getInstance()


        binding.btnsignup.setOnClickListener {
            val email = binding.edtemail.text.toString()
            val password = binding.edtpassword.text.toString()
            val username = binding.edtusername.text.toString()
            val repassword = binding.edtrepeatpass.text.toString()

            if (email.equals("")|| password.equals("")|| username.equals("")|| repassword.equals("")){
                Toast.makeText(this,"Please all the details",Toast.LENGTH_LONG).show()
            }else if (password.equals(repassword)){

                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this){
                        if (it.isSuccessful){
                            Toast.makeText(this,"User registration sucessful",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }else {
                            Toast.makeText(this,"User registration failed: ${it.exception?.message}",Toast.LENGTH_LONG).show()


                        }

                    }
            }else{
                Toast.makeText(this,"Password is not same",Toast.LENGTH_LONG).show()
            }


        }

       binding.btnsignin.setOnClickListener {

           startActivity(Intent(this,LoginActivity::class.java))

       }


    }
}