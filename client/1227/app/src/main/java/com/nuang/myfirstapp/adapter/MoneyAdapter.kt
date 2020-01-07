package com.nuang.myfirstapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.nuang.myfirstapp.ClaimItem
import com.nuang.myfirstapp.R


class MoneyAdapter(val context: Context, val itemList: ArrayList<ClaimItem>): RecyclerView.Adapter<MoneyAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.claim_item, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.claim_item_name)
        val amount = itemView. findViewById<TextView>(R.id.claim_item_amount)

        fun bind(item: ClaimItem) {
            name?.text = item.name
            amount?.text = item.amount.toString()

            itemView.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.claimer_item_dialog, null)

                val dialogName = dialogView.findViewById<TextView>(R.id.claimer_dialog_name_t)
                val dialogAmount = dialogView.findViewById<TextView>(R.id.claimer_dialog_amount_t)
                val dialogFrom = dialogView.findViewById<TextView>(R.id.claimer_dialog_from_t)
                val dialogDate = dialogView.findViewById<TextView>(R.id.claimer_dialog_date_t)
                val dialogAccount = dialogView.findViewById<TextView>(R.id.claimer_dialog_account_t)

                dialogName?.text = item.name
                dialogAmount?.text = item.amount.toString()
                dialogFrom?.text = item.claimId
                dialogDate?.text = item.date
                dialogAccount?.text = item.account

                builder.setView(dialogView)
                    .setPositiveButton("확인") {
                            dialogInterface, i ->

                    }
                    .setNegativeButton("정산") {
                        dialogInterface, i ->
                        // item.account가 빈 경우에는 ~~
                        // api 요청하기
                    }
                    .show()
            }
        }
    }
}