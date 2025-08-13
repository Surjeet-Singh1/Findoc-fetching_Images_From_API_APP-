package com.example.assigmentapp // Your package name

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
//import androidx.compose.animation.with
//import androidx.compose.ui.semantics.error
import androidx.recyclerview.widget.RecyclerView
//import androidx.wear.compose.material.placeholder
import com.bumptech.glide.Glide

class FetchedImageAdapter(private var images: List<PicsumImage>) :
    RecyclerView.Adapter<FetchedImageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.fetchedImageView)
        val authorTextView: TextView = itemView.findViewById(R.id.imageAuthorTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fetched_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]
        holder.authorTextView.text = "By: ${image.author}" // Display author

        Glide.with(holder.itemView.context)
            .load(image.downloadUrl) // Use the download_url
            .placeholder(R.drawable.ic_placeholder) // Optional: Add a placeholder drawable
            .error(R.drawable.ic_error) // Optional: Add an error drawable
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size

    // Function to update the data in the adapter
    fun updateData(newImages: List<PicsumImage>) {
        images = newImages
        notifyDataSetChanged() // Consider using DiffUtil for better performance
    }
}