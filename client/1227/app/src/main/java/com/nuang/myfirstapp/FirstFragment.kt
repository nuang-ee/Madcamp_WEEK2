package com.nuang.myfirstapp

import android.content.SharedPreferences
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_first.*
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import java.lang.Exception


class FirstFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var customAdapter: CustomAdapter? = null
    private var contactModelArrayList: ArrayList<ContactModel>? = null
    val uid: String = ""

    inner class fetchContacts: AsyncTask<Void, Void, ArrayList<ContactModel>>() {
        override fun doInBackground(vararg p0: Void): ArrayList<ContactModel>? {
            contactModelArrayList = ArrayList()
            //Cursor for getting ID&name -- Grabs whole contacts
            val cursor: Cursor? = context?.contentResolver?.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
            )

            while (cursor?.moveToNext()!!) {
                val contactmodel = ContactModel()
                val id = cursor.getString(cursor.getColumnIndex((ContactsContract.Contacts._ID)))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactmodel.setNames(name)
                val photo = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))
                if(photo == null){
                    Log.d("photo>>", "null")
                }else{
                    contactmodel.setPhoto(Uri.parse(photo))
                    Log.d("photo>>", photo)
                }

                //contactmodel.setPhoto(photo)

                val phoneCursor = context?.contentResolver?.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                    null,
                    null
                )

                if (phoneCursor != null) {
                    //if Data exists
                    if (phoneCursor.moveToFirst()) {
                        val number =
                            phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        contactmodel.setNumbers(number.toString())
                    }
                }
                phoneCursor?.close()

                val emailCursor = context?.contentResolver?.query(
                    ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + id,
                    null,
                    null
                )

                if (emailCursor != null) {
                    if (emailCursor.moveToFirst()) {
                        val email =
                            emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                        contactmodel.setMails(email.toString())
                    }
                }
                emailCursor?.close()

                //blank if null
                if (contactmodel.mail == null) contactmodel.setMails("-")
                if (contactmodel.number == null) contactmodel.setNumbers("-")

                contactModelArrayList!!.add(contactmodel)
                Log.d(
                    "info >>",
                    contactmodel.getNames() + "  " + contactmodel.getNumbers() + "  " + contactmodel.getMails()
                )
            }
            cursor.close()
            return contactModelArrayList as ArrayList<ContactModel>
        }

        override fun onPostExecute(result: ArrayList<ContactModel>?) {
            super.onPostExecute(result)
            try {
                contactModelArrayList = result
                customAdapter = context?.let { CustomAdapter(it, contactModelArrayList!!) }
                recyclerView!!.adapter = customAdapter

                val lm = LinearLayoutManager(context)
                recyclerView!!.layoutManager = lm
                recyclerView!!.setHasFixedSize(true)
            }
            catch (e: Exception) {
                Log.d("fetchContactException>>", e.toString())
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout.setOnRefreshListener {
            fetchContacts().execute()
            swipeRefreshLayout.isRefreshing = false
        }
    }


    private fun checkFirstRun() {
        val preferences: SharedPreferences = activity!!.getSharedPreferences("com.example.myfirstapp", MODE_PRIVATE)
        val isFirstRun = preferences.getBoolean("isFirstRun", true)
        if (isFirstRun) {
            fetchContacts().execute()
            preferences.edit().putBoolean("ifFirstRun", false).commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        val mouse = view.findViewById(R.id.mouse) as ImageView
        mouse.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                val intent = Intent(view.context, hihi::class.java)
                activity?.startActivity(intent)
            }
        })
        checkFirstRun()
        return view
    }
}