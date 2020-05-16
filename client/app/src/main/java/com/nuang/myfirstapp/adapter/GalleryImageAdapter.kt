package com.nuang.myfirstapp.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nuang.myfirstapp.R
import com.nuang.myfirstapp.helper.GlideApp
import kotlinx.android.synthetic.main.img_item.view.*


class GalleryImageAdapter(private val itemList: List<Image>) : RecyclerView.Adapter<GalleryImageAdapter.ViewHolder>() {
    interface ClickListener : View.OnLongClickListener {
        fun onLongClick(view: View, position: Int): Boolean
    }
    var clickListener: ClickListener? = null
    var selectedPosition = -1
    private var context: Context? = null
    var listener: GalleryImageClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImageAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.img_item, parent,
            false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: GalleryImageAdapter.ViewHolder, position: Int) {
        holder.bind()
        Log.d("Position>>", position.toString())
        /*
        holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
        if (clickListener != null) {
            holder.itemView.setOnLongClickListener { v -> clickListener?.onLongClick(v, position)!! }
        }

        if (selectedPosition == position) holder.itemView.setBackgroundColor(Color.parseColor("#000000"))
        else holder.itemView.setBackgroundColor(Color.parseColor("#FF0266"))

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            selectedPosition = position
            notifyDataSetChanged()
        })
        */

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            val image = itemList.get(adapterPosition)
            // load image
            GlideApp.with(context!!)
                .load(image.imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.ivGalleryImage)
            // adding click or tap handler for our image layout
            itemView.container.setOnClickListener {
                listener?.onClick(adapterPosition)
            }
            itemView.container.setOnLongClickListener { v ->
                clickListener?.onLongClick(v, adapterPosition)!!
            }
        }
    }
}