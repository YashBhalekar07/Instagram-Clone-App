package com.example.instagramclone.adapers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.Models.Post
import com.example.instagramclone.Models.User
import com.example.instagramclone.R
import com.example.instagramclone.databinding.PostRvBinding
import com.example.instagramclone.utils.USER_NODE
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class PostAdapter(var context: Context,var postList:ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.MyHolder>(){

    inner class MyHolder(var binding: PostRvBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var binding = PostRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
       return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try {
            Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get().addOnSuccessListener {
                var user=it.toObject<User>()!!
                Picasso.get().load(user!!.image).placeholder(R.drawable.user).into(holder.binding.profileImage) // glide
                holder.binding.name.text = user.name

            }

        }catch (e:Exception){

        }
       Picasso.get().load(postList.get(position).postUrl).placeholder(R.drawable.loading).into(holder.binding.postImage)
        try{

            val text = TimeAgo.using(postList.get(position).time.toLong())
            holder.binding.time.text = text
        }catch (e:Exception){
            holder.binding.time.text = ""
        }
        holder.binding.share.setOnClickListener {
            var i=Intent(android.content.Intent.ACTION_SEND)
            i.type="text/plane"
            i.putExtra(Intent.EXTRA_TEXT,postList.get(position).postUrl)
            context.startActivity(i)
        }
        holder.binding.caption.text=postList.get(position).caption
        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.heart_red)
        }
    }
}