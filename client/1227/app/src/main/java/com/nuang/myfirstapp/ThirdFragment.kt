package com.nuang.myfirstapp


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.nuang.myfirstapp.adapter.MoneyAdapter
import kotlinx.android.synthetic.main.fragment_third.*
import java.text.SimpleDateFormat
import java.util.*


class ThirdFragment : Fragment() {

    var claimerList = arrayListOf<ClaimItem>()
    var claimeeList = arrayListOf<ClaimItem>()

    val serverUrl = "http://34.84.158.57:4001"
    var uid: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ClaimerAdapter =
            MoneyAdapter(requireContext(), claimerList)
        claimerRecyclerView.adapter = ClaimerAdapter
        val ClaimeeAdapter =
            MoneyAdapter(requireContext(), claimeeList)
        claimeeRecyclerView.adapter = ClaimeeAdapter

        val claimerLayoutManager = LinearLayoutManager(requireContext())
        claimerRecyclerView.layoutManager = claimerLayoutManager
        claimerRecyclerView.setHasFixedSize(true)
        val claimeeLayoutManager = LinearLayoutManager(requireContext())
        claimeeRecyclerView.layoutManager = claimeeLayoutManager
        claimeeRecyclerView.setHasFixedSize(true)

        claimee_button.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.add_claimer_pop_up, null)
            val claimer_id = dialogView.findViewById<EditText>(R.id.popup_claimer_id)
            val name = dialogView.findViewById<EditText>(R.id.popup_claimer_name)
            val amount = dialogView.findViewById<EditText>(R.id.popup_claimer_amount)
            val account = dialogView.findViewById<EditText>(R.id.popup_claimer_account)

            builder.setView(dialogView)
                .setPositiveButton("확인") {
                    dialogInterface, i ->
                        //val _id =
                        val claimerId = claimer_id.text.toString()
                        val transactionName = name.text.toString()
                        val cash = Integer.parseInt(amount.text.toString())
                        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                        val date = sdf.format(Date()).toString()
                        val account_t = account.text.toString()
                        val claimee = ClaimItem(_id="0", claimId=claimerId, name=transactionName, amount=cash, date=date, account=account_t)
                        claimeeList.add(claimee)
                        ClaimeeAdapter.notifyDataSetChanged()
                }
                .setNegativeButton("취소") {
                        dialogInterface, i ->
                }
                .show()
        }
    }
}

