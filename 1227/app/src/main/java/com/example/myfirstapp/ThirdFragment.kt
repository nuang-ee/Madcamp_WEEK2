package com.example.myfirstapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.myfirstapp.helper.GpsTracker
import java.io.IOException
import java.lang.ClassCastException
import java.util.*


class ThirdFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        val textviewAddress = view.findViewById(R.id.location) as TextView
        val ShowLocationButton = view.findViewById(R.id.button) as Button
        ShowLocationButton.setOnClickListener(View.OnClickListener {
            val gpsTracker = GpsTracker(context)

            val latitude = gpsTracker.latitude
            val longitude = gpsTracker.longitude

            val address = getCurrentAddress(latitude, longitude)
            textviewAddress.text = address
            Log.d("address >>", address)

            val addressParsed = address.split("\\s")

            val cityName = addressParsed[1]
            val citySubName = addressParsed[2]

            /*
            Toast.makeText(
                context,
                "현재위치 \n위도 $latitude\n경도 $longitude",
                Toast.LENGTH_LONG
            ).show()
            */
        })
        return view
    }

    private fun getCurrentAddress(latitude: Double, longitude: Double): String {
        //지오코더... GPS를 주소로 변환
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //네트워크 문제
            Toast.makeText(context, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show()
            return "지오코더 서비스 사용불가"
        } catch (illegalArgumentException: IllegalArgumentException) {
            Toast.makeText(context, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show()
            return "잘못된 GPS 좌표"
        }

        if (addresses == null || addresses.size == 0) {
            Toast.makeText(context, "주소 미발견", Toast.LENGTH_LONG).show()
            return "주소 미발견"
        }
        val address = addresses[0]
        return address.getAddressLine(0).toString() + "\n"
    }

    /*
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragementSelected) {
            listener = context
        }
        else {
            throw ClassCastException( context.toString() + " must implement OnFragmentSelected")
        }
    }

    private lateinit var listener: OnFragementSelected
    interface OnFragementSelected {
        fun onFragmentSelected()
    }
    */
}
