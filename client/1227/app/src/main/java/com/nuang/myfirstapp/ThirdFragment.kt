package com.nuang.myfirstapp


import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.AccessToken
import com.facebook.Profile
import com.nuang.myfirstapp.adapter.CustomAdapter
import com.nuang.myfirstapp.adapter.MoneyAdapter
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.fragment_third.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ThirdFragment : Fragment() {

    var claimerList = arrayListOf<ClaimItem>()
    var claimeeList = arrayListOf<ClaimItem>()

    val serverUrl = "http://34.84.158.57:4001"
    var uid: String = ""
    var userName: String = ""

    private val claimeeItemArrayList: ArrayList<ClaimItem> = ArrayList()
    private val claimerItemArrayList: ArrayList<ClaimItem> = ArrayList()
    private var claimerAdapter: MoneyAdapter? = null
    private var claimeeAdapter: MoneyAdapter? = null

    private var claimerRecyclerView: RecyclerView? = null
    private var claimeeRecyclerView: RecyclerView? = null

    fun checkLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn) {
            uid = Profile.getCurrentProfile().id
            userName = Profile.getCurrentProfile().firstName
            Log.d("uid, userName>>", "$uid // $userName")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        checkLogin()


        val view = inflater.inflate(R.layout.fragment_third, container, false)
        claimerRecyclerView = view.findViewById(R.id.claimerRecyclerView)
        claimeeRecyclerView = view.findViewById(R.id.claimeeRecyclerView)


        claimerAdapter =
            MoneyAdapter(requireContext(), claimerItemArrayList)
        claimerRecyclerView?.adapter = claimerAdapter
        claimeeAdapter =
            MoneyAdapter(requireContext(), claimeeItemArrayList)
        claimeeRecyclerView?.adapter = claimeeAdapter

        val claimerLayoutManager = LinearLayoutManager(requireContext())
        claimerRecyclerView?.layoutManager = claimerLayoutManager
        claimerRecyclerView?.setHasFixedSize(true)

        val claimeeLayoutManager = LinearLayoutManager(requireContext())
        claimeeRecyclerView?.layoutManager = claimeeLayoutManager
        claimeeRecyclerView?.setHasFixedSize(true)


        val itemTouchHelper = claimeeAdapter?.let { SwipeToDeleteCallback(it) }?.let {
            ItemTouchHelper(
                it
            )
        }
        itemTouchHelper?.attachToRecyclerView(claimeeRecyclerView)

        val claimee_button = view.findViewById<Button>(R.id.claimee_button)
        claimee_button.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.add_claimer_pop_up, null)
            val claimeeName = dialogView.findViewById<EditText>(R.id.popup_claimer_id)
            val name = dialogView.findViewById<EditText>(R.id.popup_claimer_name)
            val amount = dialogView.findViewById<EditText>(R.id.popup_claimer_amount)
            val account = dialogView.findViewById<EditText>(R.id.popup_claimer_account)

            builder.setView(dialogView)
                .setPositiveButton("확인") { dialogInterface, i ->
                    //val _id =
                    val claimee = ClaimItem()
                    claimee.claimee = claimeeName.text.toString()
                    claimee.name = name.text.toString()
                    claimee.amount = Integer.parseInt(amount.text.toString())
                    val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                    claimee.date = sdf.format(Date()).toString()
                    claimee.account = account.text.toString()
                    addClaimee(claimee).execute()
                }
                .setNegativeButton("취소") { dialogInterface, i ->
                }
                .show()
        }
        return view
    }

    inner class SwipeToDeleteCallback(claimeeAdapter: MoneyAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        private val icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete_24px) }
        private val background = ColorDrawable(Color.RED)
        val mAdapter = claimeeAdapter
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            deleteItem(position)
        }
    }

    fun deleteItem(position: Int) {
        claimeeItemArrayList[position]._id?.let { receivedMoney(it).execute() }
        claimeeItemArrayList.removeAt(position)
        claimeeRecyclerView?.adapter?.notifyItemRemoved(position)
    }

    //val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout2.setOnRefreshListener {
            claimeeItemArrayList.clear()
            claimerItemArrayList.clear()
            initializeClaimee().execute()
            initializeClaimer().execute()

            swipeRefreshLayout2.isRefreshing = false
        }
    }



    //===========Claimer
    inner class initializeClaimer: AsyncTask<Void, Void, String?>() {
        override fun doInBackground(vararg p0: Void): String? {
            var claimerResult = ""
            try {
                val claimerUrl = URL("$serverUrl/claimer/get")
                val claimerUrlConnection = claimerUrl.openConnection() as HttpURLConnection
                claimerUrlConnection.requestMethod = "POST"

                val claimerWr = OutputStreamWriter(claimerUrlConnection.outputStream)
                claimerWr.write("uid=$uid")
                claimerWr.flush()

                BufferedReader(InputStreamReader(claimerUrlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    claimerResult = response.toString()
                }

            } catch (e: Exception) {
                Log.d("ExceptionGetClaimer>>", e.toString())
            }
            return claimerResult
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val claimItem = ClaimItem()
                    claimItem.sent = item.getBoolean("sent")
                    claimItem.received = item.getBoolean("received")
                    claimItem._id = item.getString("_id")
                    claimItem.claimer = item.getString("claimer")
                    claimItem.amount = item.getInt("amount")
                    claimItem.account = item.getString("account")
                    claimItem.name = item.getString("name")
                    claimItem.date = item.getString("date")

                    claimerItemArrayList.add(claimItem)
                    claimerRecyclerView?.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.d("ExceptionGetClaimer>>", e.toString())
            }
        }
    }


    inner class addClaimer(claimItem: ClaimItem): AsyncTask<Void, Void, String?>() {
        val tempItem = claimItem
        override fun doInBackground(vararg p0: Void): String? {
            var claimerResult = ""
            try {
                val claimerUrl = URL("$serverUrl/claimer/add")
                val claimerUrlConnection = claimerUrl.openConnection() as HttpURLConnection
                claimerUrlConnection.requestMethod = "POST"

                val claimerWr = OutputStreamWriter(claimerUrlConnection.outputStream)
                claimerWr.write("uid=${tempItem.claimeeuid}")
                claimerWr.write("&claimer=${tempItem.claimee}")
                claimerWr.write("&amount=${tempItem.amount}")
                claimerWr.write("&account=${tempItem.account}")
                claimerWr.write("&name=${tempItem.name}")
                claimerWr.write("&date=${tempItem.date}")
                Log.d("request >>", "uid=$uid"+"claimer=${tempItem.claimer}"+"amount=${tempItem.amount}"
                        +"account=${tempItem.account}"+"name=${tempItem.name}"+"date=${tempItem.date}")
                claimerWr.flush()

                BufferedReader(InputStreamReader(claimerUrlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    claimerResult = response.toString()
                }

            } catch (e: Exception) {
                Log.d("ExceptionAddClaimer>>", e.toString())
            }
            return claimerResult
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val succeed = jsonObject.getInt("result")
                if (succeed == 1) {
                    //do nothing
                }
            } catch (e: Exception) {
                Log.d("ExceptionAddClaimer>>", e.toString())
            }
        }
    }


    inner class sentMoney(id: String): AsyncTask<Void, Void, String?>() {
        val _id = id
        override fun doInBackground(vararg p0: Void): String? {
            var claimerResult = ""
            try {
                val claimerUrl = URL("$serverUrl/claimer/sent")
                val claimerUrlConnection = claimerUrl.openConnection() as HttpURLConnection
                claimerUrlConnection.requestMethod = "PUT"

                val claimerWr = OutputStreamWriter(claimerUrlConnection.outputStream)
                claimerWr.write("uid=$uid")
                claimerWr.write("&_id=$_id")
                claimerWr.flush()

                BufferedReader(InputStreamReader(claimerUrlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    claimerResult = response.toString()
                }

            } catch (e: Exception) {
                Log.d("ExceptionSentMoney>>", e.toString())
            }
            return claimerResult
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val succeed = jsonObject.getInt("result")
                if (succeed == 1) {
                    //do nothing
                }
            } catch (e: Exception) {
                Log.d("ExceptionSentMoney>>", e.toString())
            }
        }
    }

    //========Claimee
    inner class initializeClaimee: AsyncTask<Void, Void, String?>() {
        override fun doInBackground(vararg p0: Void): String? {
            var claimerResult = ""
            try {
                val claimerUrl = URL("$serverUrl/claimee/get")
                val claimerUrlConnection = claimerUrl.openConnection() as HttpURLConnection
                claimerUrlConnection.requestMethod = "POST"

                val claimerWr = OutputStreamWriter(claimerUrlConnection.outputStream)
                claimerWr.write("uid=$uid")
                claimerWr.flush()

                BufferedReader(InputStreamReader(claimerUrlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    claimerResult = response.toString()
                }

            } catch (e: Exception) {
                Log.d("ExceptionGetClaimee>>", e.toString())
            }
            return claimerResult
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val claimItem = ClaimItem()

                    claimItem.received = item.getBoolean("received")
                    claimItem.sent = item.getBoolean("sent")
                    claimItem._id = item.getString("_id")
                    claimItem.claimee = item.getString("claimee")
                    claimItem.amount = item.getInt("amount")
                    claimItem.name = item.getString("name")
                    claimItem.date = item.getString("date")

                    claimeeItemArrayList.add(claimItem)
                    claimeeRecyclerView?.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.d("ExceptionGetClaimee>>", e.toString())
            }
        }
    }


    inner class addClaimee(claimItem: ClaimItem): AsyncTask<Void, Void, String?>() {
        val tempItem = claimItem
        override fun doInBackground(vararg p0: Void): String? {
            var claimerResult = ""
            try {
                val claimerUrl = URL("$serverUrl/claimee/add")
                val claimerUrlConnection = claimerUrl.openConnection() as HttpURLConnection
                claimerUrlConnection.requestMethod = "POST"

                val claimerWr = OutputStreamWriter(claimerUrlConnection.outputStream)
                claimerWr.write("uid=$uid")
                claimerWr.write("&claimee=${tempItem.claimee}")
                claimerWr.write("&amount=${tempItem.amount}")
                claimerWr.write("&name=${tempItem.name}")
                claimerWr.write("&date=${tempItem.date}")
                Log.d("request >>", "uid=$uid"+"claimee=${tempItem.claimee}"+"amount=${tempItem.amount}"
                        +"name=${tempItem.name}"+"date=${tempItem.date}")
                claimerWr.flush()

                BufferedReader(InputStreamReader(claimerUrlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    claimerResult = response.toString()
                }

            } catch (e: Exception) {
                Log.d("ExceptionAddClaimee>>", e.toString())
            }
            return claimerResult
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val succeed = jsonObject.getInt("result")
                if (succeed == 1) {
                    val claimItem = ClaimItem()
                    claimItem._id = jsonObject.getString("_id")
                    claimItem.claimeeuid = jsonObject.getString("uid")
                    claimItem.claimee = tempItem.claimee
                    claimItem.amount = tempItem.amount
                    claimItem.name = tempItem.name
                    claimItem.date = tempItem.date
                    claimItem.account = tempItem.account

                    claimItem.claimeruid = uid
                    claimItem.claimer = userName

                    claimeeItemArrayList.add(claimItem)
                    claimeeAdapter?.notifyDataSetChanged()

                    addClaimer(claimItem).execute()
                    claimeeRecyclerView?.adapter = claimeeAdapter
                }
            } catch (e: Exception) {
                Log.d("ExceptionAddClaimee>>", e.toString())
            }
        }
    }


    inner class receivedMoney(id: String): AsyncTask<Void, Void, String?>() {
        val _id = id
        override fun doInBackground(vararg p0: Void): String? {
            var claimerResult = ""
            try {
                val claimerUrl = URL("$serverUrl/claimee/recieved")
                val claimerUrlConnection = claimerUrl.openConnection() as HttpURLConnection
                claimerUrlConnection.requestMethod = "PUT"

                val claimerWr = OutputStreamWriter(claimerUrlConnection.outputStream)
                claimerWr.write("uid=$uid")
                claimerWr.write("&_id=$_id")
                claimerWr.flush()

                BufferedReader(InputStreamReader(claimerUrlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    claimerResult = response.toString()
                }

            } catch (e: Exception) {
                Log.d("ExceptionRecieved>>", e.toString())
            }
            return claimerResult
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val succeed = jsonObject.getInt("result")
                if (succeed == 1) {
                    //do nothing
                }
            } catch (e: Exception) {
                Log.d("ExceptionReceived>>", e.toString())
            }
        }
    }



}

