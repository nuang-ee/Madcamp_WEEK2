package com.nuang.myfirstapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nuang.myfirstapp.ContactModel
import com.nuang.myfirstapp.R
import com.nuang.myfirstapp.adapter.CustomAdapter.ViewHolder


class CustomAdapter(private val context: Context, private val contactModelArrayList: ArrayList<ContactModel>): RecyclerView.Adapter<ViewHolder>() {
    val serverUrl = "http://34.84.158.57:4001"

    interface ClickListener : View.OnLongClickListener {
        fun onLongClick(view: View, position: Int): Boolean
    }
    var clickListener: ClickListener? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (clickListener != null) {
            holder.itemView.setOnLongClickListener { v -> clickListener?.onLongClick(v, position)!! }
        }
    }

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var tvname: TextView? = itemView?.findViewById(R.id.name)
        var tvnumber: TextView? = itemView?.findViewById(R.id.number)
        var tvmail: TextView? = itemView?.findViewById(R.id.mail)
        //var tvimage: ImageView? = itemView?.findViewById(R.id.profile_image)

        fun bind (contactModel: ContactModel) {
            tvname?.text = contactModel.getNames()
            tvnumber?.text = contactModel.getNumbers()
            tvmail?.text = contactModel.getMails()
            /*
            if(contactModel.getPhoto() != null){
                tvimage?.setImageURI(Uri.parse(serverUrl + "/static/" + contactModel.getPhoto()))
            }
             */
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactModelArrayList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return contactModelArrayList.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
}