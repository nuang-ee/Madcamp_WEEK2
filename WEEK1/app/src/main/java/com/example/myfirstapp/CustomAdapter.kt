package com.example.myfirstapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.CustomAdapter.ViewHolder


class CustomAdapter(private val context: Context, private val contactModelArrayList: ArrayList<ContactModel>): RecyclerView.Adapter<ViewHolder>() {
    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {

        var tvname: TextView? = itemView?.findViewById(R.id.name)
        var tvnumber: TextView? = itemView?.findViewById(R.id.number)

        fun bind (contactModel: ContactModel, context: Context) {
            tvname?.text = contactModel.getNames()
            tvnumber?.text = contactModel.getNumbers()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.lv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contactModelArrayList[position], context)
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

    /*
    fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.lv_item, null, true)

            holder.tvname = convertView!!.findViewById(R.id.name) as TextView
            holder.tvnumber = convertView.findViewById(R.id.number) as TextView

            convertView.tag = holder
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = convertView.tag as ViewHolder
        }

        holder.tvname!!.setText(contactModelArrayList[position].getNames())
        holder.tvnumber!!.setText(contactModelArrayList[position].getNumbers())

        return convertView
    }
*/

}