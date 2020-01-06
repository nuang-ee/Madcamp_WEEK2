package com.nuang.myfirstapp

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
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class FirstFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var customAdapter: CustomAdapter? = null
    private var contactModelArrayList: ArrayList<ContactModel>? = null
    var uid: String = ""
    val serverUrl = "http://34.84.158.57:4001"

    fun refreshContacts() {
        customAdapter = context?.let { CustomAdapter(it, contactModelArrayList!!) }
        recyclerView!!.adapter = customAdapter
        val lm = LinearLayoutManager(context)
        recyclerView!!.layoutManager = lm
        recyclerView!!.setHasFixedSize(true)
    }

    inner class initialize: AsyncTask<Void, Void, String?>() {
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
                        contactmodel.mail = item.getString("email")
                        contactmodel.number = item.getString("phoneNumber")
                        contactmodel.photoName = item.getString("thumbnail")
                        //contactmodel.photoUri = Uri.parse("$serverUrl/static/$thumbnailName")
                        contactmodel.id = item.getString("_id")
                    }
                    contactModelArrayList!!.add(contactmodel)
                }
                refreshContacts()
            } catch (e: Exception) {
                Log.d("fetchContactException>>", e.toString())
            }
        }
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
                    contactModelArrayList!!.add(tempContactModel)
                }
                else Log.d("AddContactFailed>>", "aa")
                refreshContacts()
            } catch (e: Exception) {
                Log.d("AddContactException>>", e.toString())
            }
        }
    }

    inner class delete internal constructor(contactId: String): AsyncTask<Void, Void, String?>() {
        val cId = contactId
        override fun doInBackground(vararg p0: Void): String? {
            var result = ""
            try {
                val url = URL("$serverUrl/contact/")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "DELETE"

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
                    for (item in contactModelArrayList!!) {
                        if (item.id == cId) contactModelArrayList!!.remove(item)
                    }
                } else Log.d("DELETE>>", " failed")
            }
            catch (e: Exception) {
                Log.d("ExceptionDeleteContacts", e.toString())
            }
        }
    }


/*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout.setOnRefreshListener {
            initialize().execute()
            swipeRefreshLayout.isRefreshing = false
        }
    }

 */


    private fun checkFirstRun() {
        val preferences: SharedPreferences = activity!!.getSharedPreferences("com.example.myfirstapp", MODE_PRIVATE)
        val isFirstRun = preferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            initialize().execute()
            preferences.edit().putBoolean("ifFirstRun", false).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            uid = savedInstanceState.getString("uid")
        }
        Log.d("uidFragment>>", uid)
        if (uid == ""){
            Log.d("fuck", "fuck")
        }
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView

        checkFirstRun()
        return view
    }
}