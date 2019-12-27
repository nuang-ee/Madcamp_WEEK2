package com.example.myfirstapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var customAdapter: CustomAdapter? = null
    private var contactModelArrayList: ArrayList<ContactModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var runflag: Boolean = true

        while (runflag) {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.READ_CONTACTS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        Manifest.permission.READ_CONTACTS
                    )
                ) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(
                        this@MainActivity,
                        arrayOf(Manifest.permission.READ_CONTACTS),
                        1
                    )

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {

                recyclerView = findViewById(R.id.recyclerView) as RecyclerView

                contactModelArrayList = ArrayList()

                val phones = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
                )
                while (phones!!.moveToNext()) {
                    val name =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    //val mail = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.CONTENT_URI))

                    val contactModel = ContactModel()
                    contactModel.setNames(name)
                    contactModel.setNumbers(phoneNumber)
                    contactModelArrayList!!.add(contactModel)
                    Log.d("name>>", name + "  " + phoneNumber)
                }
                phones.close()
                runflag = false

                customAdapter = CustomAdapter(this, contactModelArrayList!!)
                recyclerView!!.adapter = customAdapter

                val lm = LinearLayoutManager(this)
                recyclerView!!.layoutManager = lm
                recyclerView!!.setHasFixedSize(true)
            }
        }
    }
}
