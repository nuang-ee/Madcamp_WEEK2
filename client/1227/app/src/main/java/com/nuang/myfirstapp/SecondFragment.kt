package com.nuang.myfirstapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nuang.myfirstapp.adapter.GalleryImageAdapter
import com.nuang.myfirstapp.adapter.GalleryImageClickListener
import com.nuang.myfirstapp.adapter.Image
import com.nuang.myfirstapp.helper.GalleryFullscreenActivity
import kotlinx.android.synthetic.main.fragment_second.*
import java.io.Serializable

class SecondFragment : Fragment(), GalleryImageClickListener {
    private val SPAN_COUNT = 3
    private val imageList = ArrayList<Image>()
    lateinit var galleryAdapter: GalleryImageAdapter
    private var recyclerView: RecyclerView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        SecondSwipeRefreshLayout.setOnRefreshListener {
            readFild()
            SecondSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView
        recyclerView!!.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        recyclerView!!.adapter = galleryAdapter

        Log.d("read", "read")
        readFild()

        return view
    }

    private fun readFild(){
        Log.d("read>>", "image")
        imageList.clear()
        var externalUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        var proj = arrayOf (MediaStore.Images.Media._ID ,
                    MediaStore.Images.Media.DISPLAY_NAME ,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.Media.DATA)
        var cursor = context?.getContentResolver()?.query(externalUri, proj, null, null, null)

        val columnIndexId = cursor?.getColumnIndex(MediaStore.Images.Media._ID)
        val columnIndexData = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursor!!.moveToNext()) {
            var imageName = cursor.getString(columnIndexData!!)
            var imageNameList = imageName?.split("/")
            imageName = imageNameList?.get(imageNameList.size -1)
            imageName = imageName?.split(".")?.get(0)
            var imageUri = externalUri.toString() + '/' +cursor.getString(columnIndexId!!)
            val Photo = Image(imageUri, imageName!!)
            if(Photo in imageList){

            }else {
                imageList.add(Photo)
            }
        }

        cursor.close()
        galleryAdapter.notifyDataSetChanged()
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
}