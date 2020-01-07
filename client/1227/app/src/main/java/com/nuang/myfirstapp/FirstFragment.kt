package com.example.myfirstapp

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_first.*
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.nuang.myfirstapp.ContactModel
import com.nuang.myfirstapp.adapter.CustomAdapter
import com.nuang.myfirstapp.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class FirstFragment : Fragment() {
    val serverUrl = "http://34.84.158.57:4001"
    var uid: String = ""
    var userName: String = ""

    private var recyclerView: RecyclerView? = null
    private var customAdapter: CustomAdapter? = null
    private var contactModelArrayList: ArrayList<ContactModel> = ArrayList()


    fun initiateView() {
        customAdapter = context?.let {
            CustomAdapter(
                it,
                contactModelArrayList
            )
        }
        customAdapter?.clickListener = object: CustomAdapter.ClickListener {
            override fun onLongClick(p0: View?): Boolean {
                return true
            }

            override fun onLongClick(view: View, position: Int): Boolean {
                //do update
                longPressed(position)
                return true
            }
        }
        recyclerView!!.adapter = customAdapter

        val lm = LinearLayoutManager(context)
        recyclerView!!.layoutManager = lm
        recyclerView!!.setHasFixedSize(true)
        val itemTouchHelper = customAdapter?.let { SwipeToDeleteCallback(it) }?.let {
            ItemTouchHelper(
                it
            )
        }
        itemTouchHelper?.attachToRecyclerView(recyclerView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout.setOnRefreshListener {
            contactModelArrayList.clear()
            initializeContacts().execute()
            swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun checkFirstRun() {
        val preferences: SharedPreferences = activity!!.getSharedPreferences("com.example.myfirstapp", MODE_PRIVATE)
        val isFirstRun = preferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            initiateView()
            initializeContacts().execute()
            preferences.edit().putBoolean("ifFirstRun", false).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            uid = savedInstanceState.getString("uid")
        } else {
            Log.d("Uid is null>>", "fuck")
        }
        checkLogin()
        checkUser().execute()
        Log.d("uidFragment>>", uid)
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton1)
        fab.setOnClickListener {
            onFabClicked()
        }

        checkFirstRun()
        return view
    }

    fun checkLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn) {
            uid = Profile.getCurrentProfile().id
            userName = Profile.getCurrentProfile().firstName
            checkUser().execute()
        }
    }

    //===============Classes For AsyncTasks
    inner class initializeContacts: AsyncTask<Void, Void, String?>() {
        override fun doInBackground(vararg p0: Void): String? {
            var result = ""
            try {
                val url = URL("$serverUrl/contact/get")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"

                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write("uid=$uid")
                wr.flush()

                BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    result = response.toString()
                }
            } catch (e: Exception) {
                Log.d("ExceptionFetchContacts", e.toString())
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val contactmodel = ContactModel()
                    val localCached = item.getBoolean("localCached")
                    if (localCached) {
                        contactmodel.name = item.getString("name")
                        Log.d("fetchedContact>>", contactmodel.name)
                        contactmodel.mail = item.getString("email")
                        contactmodel.number = item.getString("phoneNumber")
                        if (item.has("thumbnail")) contactmodel.photoName = item.getString("thumbnail")
                        //contactmodel.photoUri = Uri.parse("$serverUrl/static/$thumbnailName")
                        contactmodel.id = item.getString("_id")
                        contactModelArrayList.add(contactmodel)
                    }
                    recyclerView?.adapter?.notifyDataSetChanged()
                }

            } catch (e: Exception) {
                Log.d("fetchContactException>>", e.toString())
            }
        }
    }

    fun longPressed(position: Int) {
        val contactModel = contactModelArrayList[position]
        val factory = LayoutInflater.from(context)
        val textEntryView = factory.inflate(R.layout.contact_entry, null)
        val input1 = textEntryView.findViewById<EditText>(R.id.contactName)
        val input2 = textEntryView.findViewById<EditText>(R.id.phoneNumber)
        val input3 = textEntryView.findViewById<EditText>(R.id.emailAddress)


        input1.setText(contactModel.name, TextView.BufferType.EDITABLE)
        input2.setText(contactModel.number, TextView.BufferType.EDITABLE)
        input3.setText(contactModel.mail, TextView.BufferType.EDITABLE)


        val alert = this.context?.let { it1 -> AlertDialog.Builder(it1) }
        alert?.setTitle("Edit Contact")?.setView(textEntryView)
            ?.setPositiveButton("Save") { _: DialogInterface?, _: Int ->
                contactModel.name = input1.text.toString()
                contactModel.number = input2.text.toString()
                contactModel.mail = input3.text.toString()
                updateContact(contactModel).execute()
                recyclerView?.adapter?.notifyItemChanged(position)
            }?.setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                //do nothing
            }?.show()
    }


    fun onFabClicked() {
        val contactModel = ContactModel()
        val factory = LayoutInflater.from(context)
        val textEntryView = factory.inflate(R.layout.contact_entry, null)
        val input1 = textEntryView.findViewById<EditText>(R.id.contactName)
        val input2 = textEntryView.findViewById<EditText>(R.id.phoneNumber)
        val input3 = textEntryView.findViewById<EditText>(R.id.emailAddress)

/*
        input1.setText("Name", TextView.BufferType.EDITABLE)
        input2.setText("Phone Number", TextView.BufferType.EDITABLE)
        input3.setText("E-mail", TextView.BufferType.EDITABLE)
*/

        val alert = this.context?.let { it1 -> AlertDialog.Builder(it1) }
        alert?.setTitle("New Contact")?.setView(textEntryView)
            ?.setPositiveButton("Save") { _: DialogInterface?, _: Int ->
                contactModel.name = input1.text.toString()
                contactModel.number = input2.text.toString()
                contactModel.mail = input3.text.toString()
                addContact(contactModel).execute()
            }?.setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                //do nothing
            }?.show()
    }


    inner class addContact(newcontact: ContactModel): AsyncTask<Void, Void, String?>() {
        val tempContactModel = newcontact
        override fun doInBackground(vararg p0: Void?): String? {
            var result = ""
            try {
                var queryparam = "uid=$uid"
                if (tempContactModel.name != null) queryparam += "&name=${tempContactModel.name}"
                if (tempContactModel.number != null) queryparam += "&phoneNumber=${tempContactModel.number}"
                if (tempContactModel.mail != null) queryparam += "&email=${tempContactModel.mail}"
                if (tempContactModel.photoName != null) queryparam += "&thumbnail=${tempContactModel.photoName}"
                queryparam += "&localCached=true"

                Log.d("UpdateContactParam", queryparam)

                val url = URL("$serverUrl/contact/add")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"

                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write(queryparam)
                wr.flush()

                BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    result = response.toString()
                }
            } catch (e: Exception) {
                Log.d("ExceptionAddContacts", e.toString())
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                if (jsonObject.getInt("result") == 1) {
                    tempContactModel.id = jsonObject.getString("_id")
                    contactModelArrayList.add(tempContactModel)
                }
                else Log.d("AddContactFailed>>", "aa")
                recyclerView?.adapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.d("AddContactException>>", e.toString())
            }
        }
    }


    inner class deleteContact internal constructor(contactId: String): AsyncTask<Void, Void, String?>() {
        val cId = contactId
        override fun doInBackground(vararg p0: Void): String? {
            var result = ""
            try {
                val url = URL("$serverUrl/contact/delete")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "DELETE"

                Log.d("deletePayload>>", "uid=$uid&_id=$cId")

                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write("uid=$uid&_id=$cId")
                wr.flush()

                BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    result = response.toString()
                }
            } catch (e: Exception) {
                Log.d("ExceptionFetchContacts", e.toString())
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val succeed = jsonObject.getInt("result")
                if (succeed == 1) {
                    Log.d("DELETE>>", "succeed")
                } else Log.d("DELETE>>", " failed")
            }
            catch (e: Exception) {
                Log.d("ExceptionDeleteContacts", e.toString())
            }
        }
    }


    inner class updateContact internal constructor(modified: ContactModel): AsyncTask<Void, Void, String?>() {
        val tempContactModel = modified
        val contactId = tempContactModel.id
        override fun doInBackground(vararg p0: Void?): String? {
            var result = ""
            try {
                val url = URL("$serverUrl/contact/update")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "PUT"

                var queryparam = "uid=$uid&_id=$contactId"
                if (tempContactModel.name != null) queryparam += "&name=${tempContactModel.name}"
                if (tempContactModel.number != null) queryparam += "&phoneNumber=${tempContactModel.number}"
                if (tempContactModel.mail != null) queryparam += "&email=${tempContactModel.mail}"
                if (tempContactModel.photoName != "") queryparam += "&thumbnail=${tempContactModel.photoName}"
                queryparam += "&localCached=true"

                Log.d("UpdateContactParam", queryparam)

                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write(queryparam)
                wr.flush()

                BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    result = response.toString()
                }
            } catch (e: Exception) {
                Log.d("ExceptionUpdateContacts", e.toString())
            }
            return result
        }

        @SuppressLint("LongLogTag")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                if (jsonObject.getInt("result") == 1) {
                    Log.d("UpdateContactSucceed>>", "aa")
                }
                else Log.d("UpdateContactFailed>>", "aa")
            } catch (e: Exception) {
                Log.d("UpdateContactException>>", e.toString())
            }
        }
    }



    //========User check
    inner class checkUser: AsyncTask<Void, Void, String?>() {
        override fun doInBackground(vararg p0: Void?): String? {
            var result = ""
            try {
                Log.d("checkUserUid>>", uid)
                val url = URL("$serverUrl/user/check")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"

                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write("uid=$uid")
                wr.flush()

                BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    result = response.toString()
                }
            } catch (e: Exception) {
                Log.d("ExceptionFetchContacts", e.toString())
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                if(jsonObject.getInt("result") == 0) {
                    //Add user
                    Log.d("checkUser>>", "result is 0")
                    addUser().execute()

                }
            } catch (e: Exception) {
                Log.d("checkUser>>", e.toString())
            }
        }
    }

    inner class addUser: AsyncTask<Void, Void, String?>() {
        override fun doInBackground(vararg p0: Void?): String? {
            var result = ""
            try {
                val url = URL("$serverUrl/user/add")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"

                val wr = OutputStreamWriter(urlConnection.outputStream)
                wr.write("uid=$uid&name=$userName")
                wr.flush()

                BufferedReader(InputStreamReader(urlConnection.inputStream)).use {
                    val response = StringBuffer()
                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    result = response.toString()
                }
            } catch (e: Exception) {
                Log.d("ExceptionFetchContacts", e.toString())
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                if(jsonObject.getInt("result") == 1) {
                    //success
                    Log.d("addUser>>", "succeed")
                }
                else Log.d("addUser>>", "failed")
            } catch (e: Exception) {
                Log.d("addUser>>", e.toString())
            }
        }
    }

    //==================Swipe To delete========
    private var recentlyDeletedItemPosition = -1
    private var recentlyDeletedItem: ContactModel? = null


    inner class SwipeToDeleteCallback(customAdapter: CustomAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        private val icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete_24px) }
        private val background = ColorDrawable(Color.RED)
        val mAdapter = customAdapter
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
        recentlyDeletedItem = contactModelArrayList[position]
        contactModelArrayList[position].id?.let { deleteContact(it).execute() }

        recentlyDeletedItemPosition = position
        contactModelArrayList.removeAt(position)
        recyclerView?.adapter?.notifyItemRemoved(position)
        showUndoSnackbar()
    }

    fun showUndoSnackbar() {
        val snackbar = Snackbar.make(
            swipeRefreshLayout, "Deleted Contact",
            Snackbar.LENGTH_LONG
        )
            .setAction("undo") {
                undoDelete()
            }.show()

    }

    fun undoDelete()
    {
        /*
        recentlyDeletedItem?.let {
            contactModelArrayList.add(
                recentlyDeletedItemPosition,
                it
            )
        }
        */
        recentlyDeletedItem?.let { addContact(it).execute() }
        recyclerView?.adapter?.notifyItemInserted(recentlyDeletedItemPosition)

    }
}