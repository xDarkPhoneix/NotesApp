package com.example.loginsignup

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.databinding.ActivityNotesBinding
import com.example.loginsignup.databinding.ActivityNotesViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotesView : AppCompatActivity(),NotesAdapter.OnItemClickListener{
    val binding by lazy {
       ActivityNotesViewBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseReference=FirebaseDatabase.getInstance().reference
        auth=FirebaseAuth.getInstance()
        recyclerView=binding.recyclerview
        recyclerView.layoutManager=LinearLayoutManager(this)

        val currentuser=auth.currentUser
        currentuser?.let {
            user->

            if(user!=null){

                val notesrefremce=databaseReference.child("users").child(user.uid).child("notes")
                notesrefremce.addValueEventListener(object:ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val notelist= mutableListOf<Noteitem>()
                        for (notesnapshot in snapshot.children){

                            val note=notesnapshot.getValue(Noteitem::class.java)
                            note?.let {
                                notelist.add(it)
                            }
                        }
                        notelist.reverse()
                        val adapter=NotesAdapter(notelist,this@NotesView)
                        recyclerView.adapter=adapter

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }


                } )

            }else{
                Toast.makeText(this,"Current user is empty",Toast.LENGTH_LONG).show()
            }

        }

    }

    override fun onUpdateclick(noteId: String) {




    }

    override fun onDeleteclick(noteId: String) {

        val currentuser=auth.currentUser
        currentuser?.let { user->

            val reference=databaseReference.child("users").child(user.uid).child("notes")
            reference.child(noteId).removeValue()
        }

    }
}