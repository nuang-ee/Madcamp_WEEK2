package com.nuang.myfirstapp

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.AccessToken
import com.facebook.Profile
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nuang.myfirstapp.adapter.GalleryImageAdapter
import com.nuang.myfirstapp.adapter.GalleryImageClickListener
import com.nuang.myfirstapp.adapter.Image
import com.nuang.myfirstapp.helper.GalleryFullscreenActivity
import kotlinx.android.synthetic.main.fragment_second.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

private const val IMAGE_PICK_CODE = 1000

class SecondFragment : Fragment(), GalleryImageClickListener {
    val serverUrl = "http://34.84.158.57:4001"
    var uid: String = ""

    private val SPAN_COUNT = 3
    private val imageList = ArrayList<Image>()
    private var recyclerView: RecyclerView? = null
    lateinit var galleryAdapter: GalleryImageAdapter
    private val imageSendList = ArrayList<Uri>()

    //initiates gallery view
    fun initiateView() {
        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this
        galleryAdapter?.clickListener = object: GalleryImageAdapter.ClickListener {
            override fun onLongClick(p0: View?): Boolean {
                return true
            }

            override fun onLongClick(view: View, position: Int): Boolean {
                //do update
                longPressed(position)
                return true
            }
        }
        recyclerView!!.adapter = galleryAdapter
        recyclerView!!.layoutManager = GridLayoutManager(context, SPAN_COUNT)
    }

    //when swipe refreshed
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SecondSwipeRefreshLayout.setOnRefreshListener {
            initializeGallery().execute()
            SecondSwipeRefreshLayout.isRefreshing = false
        }
    }

    fun longPressed(position: Int) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkLogin()
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        recyclerView = view?.findViewById(R.id.recyclerView) as? RecyclerView
        initiateView()

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton1)
        fab.setOnClickListener {
            onFabClicked()
        }

        return view
    }

    override fun onClick(position: Int) {
        //val bundle = Bundle()
        //bundle.putSerializable("images", imageList)
        //bundle.putInt("position", position)
        val intent = Intent(activity, GalleryFullscreenActivity::class.java)
        intent.putExtra("images", imageList as Serializable)
        intent.putExtra("position", position)
        startActivity(intent)
    }


    fun checkLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if(isLoggedIn) {
            uid = Profile.getCurrentProfile().id
        }
    }

    fun onFabClicked() {
        pickImageFromGallery()
    }


    /**
     *  pickImageFromGallery()
     *  through intent, call gallery activity
     *  go to gallery activity, and get some image
     */
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK) //action get content 고려
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //activity 간의 인수와 리턴값을 전달(저장)
        startActivityForResult(Intent.createChooser(intent, "Select picture"), IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (data == null) {
                Log.d("imageAddFailed>>", "something's wrong")
            }
            Log.d("data>>", "data")
            Log.d("data>>", data.toString())
            val clipData = data?.clipData
            if (clipData != null) {
                for (i in 0 until clipData.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    Log.d("imageUri>>", imageUri.toString())

                    val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val byteArray = stream.toByteArray()
                    addPictures(byteArray)
                    //imageSendList.add(imageUri)
                }
            }
            galleryAdapter.notifyDataSetChanged()
        }
    }


    //AsyncTasks
    inner class initializeGallery: AsyncTask<Void, Void, String?>() {
        override fun doInBackground(vararg p0: Void): String? {
            var result = ""
            try {
                val url = URL("$serverUrl/image/get")
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
                Log.d("ExceptionFetchGallery>>", e.toString())
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONObject(i)
                    val localCached = item.getBoolean("localCached")
                    if (localCached) {
                        val imageUrl = item.getString("contentUrl")
                        val imageId = item.getString("_id")
                        Log.d("fetchedImage>>", "$imageUrl || $imageId")
                        val imageObject = Image(serverUrl+imageUrl, "==", imageId)
                        imageList.add(imageObject)
                    }
                    recyclerView?.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.d("fetchGalleryException>>", e.toString())
            }
        }
    }

    inner class addPictures(image: ByteArray): AsyncTask<Void, Void, String?>() {
        val imageByteArray = image
        override fun doInBackground(vararg p0: Void): String? {
            val attachmentName = "bitmap"
            val attachmentFileName = "bitmap.bmp"
            val crlf = "\r\n"
            val twoHyphens = "--"
            val boundary = "*****"

            var result = ""
            try {
                val url = URL("$serverUrl/image/add")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.setRequestProperty(
                    "Content-Type", "multipart/form-data;boundary=$boundary"
                )

                val wr = OutputStreamWriter(urlConnection.outputStream)
                val request = DataOutputStream(urlConnection.outputStream)
                request.writeBytes(twoHyphens + boundary + crlf)
                request.writeBytes("uid=$uid")
                request.writeBytes("Content-Disposition: form-data; name=\"" +
                        attachmentName + "\";filename=\"" +
                        attachmentFileName + "\"" + crlf)
                request.writeBytes(crlf)
                request.write(imageByteArray)
                request.writeBytes(crlf)
                request.writeBytes(twoHyphens + boundary + twoHyphens + crlf)
                request.flush()
                request.close()

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
                Log.d("ExceptionAddImage>>", e.toString())
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val result = jsonObject.getInt("result")
                if (result == 1) {
                    val imageUrl = jsonObject.getString("contentUrl")
                    val imageId = jsonObject.getString("_id")
                    Log.d("fetchedImage>>", "$imageUrl || $imageId")
                    val imageObject = Image(serverUrl + imageUrl, "==", imageId)
                    imageList.add(imageObject)
                }
                recyclerView?.adapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                Log.d("fetchGalleryException>>", e.toString())
            }
        }
    }

}