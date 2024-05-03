package com.example.loginsignup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginsignup.databinding.ActivityNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NotesActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityNotesBinding.inflate(layoutInflater)
    }
  private lateinit var auth: FirebaseAuth
    private lateinit var databaseref:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        databaseref=FirebaseDatabase.getInstance().reference
        auth=FirebaseAuth.getInstance()

        binding.btnsubmitnote.setOnClickListener {
            val title=binding.edttitle.text.toString()
            val content=binding.edtcontent.text.toString()

            if(title.isEmpty() && content.isEmpty()){
                Toast.makeText(this,"Fill the contents",Toast.LENGTH_LONG).show()

            }else{
                val currentuser=auth.currentUser
                currentuser?.let { user->

                    val notekey=databaseref.child("users").child(user.uid).child("notes").push().key
                    val noteitem=Noteitem(title,content,notekey?:"")

                    if(notekey!=null){
                        databaseref.child("users").child(user.uid).child("notes").child(notekey).setValue(noteitem)
                            .addOnCompleteListener{task->
                                if(task.isSuccessful){
                                    Toast.makeText(this,"Note added successfully",Toast.LENGTH_LONG).show()
                                    finish()
                                }else{
                                    Toast.makeText(this,"Note not added successfully",Toast.LENGTH_LONG).show()

                                }
                            }

                    }else{
                        Toast.makeText(this,"Failed to save note",Toast.LENGTH_LONG).show()
                    }

                }


            }

        }

    }
}