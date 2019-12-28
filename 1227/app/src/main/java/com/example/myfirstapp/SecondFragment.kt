package com.example.myfirstapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstapp.adapter.GalleryImageAdapter
import com.example.myfirstapp.adapter.GalleryImageClickListener
import com.example.myfirstapp.adapter.Image
import com.example.myfirstapp.fragment.GalleryFullscreenFragment


class SecondFragment : Fragment(), GalleryImageClickListener {
    private val SPAN_COUNT = 3
    private val imageList = ArrayList<Image>()
    lateinit var galleryAdapter: GalleryImageAdapter
    private var recyclerView: RecyclerView? = null

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

        loadImages()

        return view
    }

    private fun loadImages() {
        imageList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        imageList.add(Image("https://i.ibb.co/gM5NNJX/butterfly.jpg", "Butterfly"))
        imageList.add(Image("https://i.ibb.co/10fFGkZ/car-race.jpg", "Car Racing"))
        imageList.add(Image("https://i.ibb.co/ygqHsHV/coffee-milk.jpg", "Coffee with Milk"))
        imageList.add(Image("https://i.ibb.co/7XqwsLw/fox.jpg", "Fox"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        imageList.add(Image("https://i.ibb.co/wc9rSgw/desserts.jpg", "Desserts Table"))
        imageList.add(Image("https://i.ibb.co/wdrdpKC/kitten.jpg", "Kitten"))
        imageList.add(Image("https://i.ibb.co/dBCHzXQ/paris.jpg", "Paris Eiffel"))
        imageList.add(Image("https://i.ibb.co/JKB0KPk/pizza.jpg", "Pizza Time"))
        imageList.add(Image("https://i.ibb.co/VYYPZGk/salmon.jpg", "Salmon "))
        imageList.add(Image("https://i.ibb.co/JvWpzYC/sunset.jpg", "Sunset in Beach"))
        imageList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        imageList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        imageList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        imageList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        imageList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        imageList.add(Image("https://i.ibb.co/wBYDxLq/beach.jpg", "Beach Houses"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        imageList.add(Image("https://i.ibb.co/L1m1NxP/girl.jpg", "Mountain Girl"))
        galleryAdapter.notifyDataSetChanged()
    }

    override fun onClick(position: Int) {
        val bundle = Bundle()
        bundle.putSerializable("images", imageList)
        bundle.putInt("position", position)
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        val galleryFragment = GalleryFullscreenFragment()
        galleryFragment.setArguments(bundle)
        galleryFragment.show(fragmentTransaction, "gallery")
    }
}