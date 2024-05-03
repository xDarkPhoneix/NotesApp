package com.example.loginsignup

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignup.NotesAdapter.ViewHolder
import com.example.loginsignup.databinding.AllnotesLayoutBinding

class NotesAdapter(private  val note :List<Noteitem>,private  val OnItemclicklistnee:OnItemClickListener) :RecyclerView.Adapter<NotesAdapter.ViewHolder>(){

    interface OnItemClickListener{
        fun onUpdateclick(noteId:String)
        fun onDeleteclick(noteId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=AllnotesLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)

    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note=note[position]
           holder.bind(note)
        holder.binding.btnupdate.setOnClickListener {
            OnItemclicklistnee.onUpdateclick(note.noteId)

        }
        holder.binding.btndelete.setOnClickListener {
            OnItemclicklistnee.onDeleteclick(note.noteId)

        }

    }

     override fun getItemCount(): Int {
           return  note.size
    }


    class ViewHolder(val binding: AllnotesLayoutBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Noteitem) {

            binding.txttitle.text=note.title
            binding.txtcontent.text=note.content
        }

    }




}