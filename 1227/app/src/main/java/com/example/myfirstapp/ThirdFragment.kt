package com.example.myfirstapp


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.myfirstapp.helper.GpsTracker
import org.json.JSONObject
import java.io.IOException
import java.lang.ClassCastException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ThirdFragment : Fragment() {
    val weatherAPI: String = "e2054e5fd85d82fb049fe2c791bfe058"
    val fineDustAPI: String = "uo81tqCqaqN2cI45bKhC8%2BFOaEg6hvoiCxLRIDx1Ks4vPqUmHUHovDuiJJanHpbYEUvnCt2U4BdcoKgsUswjkQ%3D%3D"
    var latitude: Double = 0.0
    var longitude: Double = 0.0

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

            latitude = gpsTracker.latitude
            longitude = gpsTracker.longitude

            val address = getCurrentAddress(latitude, longitude)
            textviewAddress.text = address
            Log.d("address >>", address)

            val addressParsed = address.split("\\s")

            val cityName = addressParsed[1]
            val citySubName = addressParsed[2]
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

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.VISIBLE
            view?.findViewById<RelativeLayout>(R.id.mainContainer)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.errorText)?.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&units=metric&appid=$weatherAPI").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp: " + main.getString("temp_min")+"°C"
                val tempMax = "Max Temp: " + main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name")+", "+sys.getString("country")

                /* Populating extracted data into our views */
                view?.findViewById<TextView>(R.id.address)?.text = address
                view?.findViewById<TextView>(R.id.updated_at)?.text =  updatedAtText
                view?.findViewById<TextView>(R.id.status)?.text = weatherDescription.capitalize()
                view?.findViewById<TextView>(R.id.temp)?.text = temp
                view?.findViewById<TextView>(R.id.temp_min)?.text = tempMin
                view?.findViewById<TextView>(R.id.temp_max)?.text = tempMax
                view?.findViewById<TextView>(R.id.sunrise)?.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                view?.findViewById<TextView>(R.id.sunset)?.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                view?.findViewById<TextView>(R.id.wind)?.text = windSpeed
                view?.findViewById<TextView>(R.id.pressure)?.text = pressure
                view?.findViewById<TextView>(R.id.humidity)?.text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
                view?.findViewById<RelativeLayout>(R.id.mainContainer)?.visibility = View.VISIBLE

            } catch (e: Exception) {
                view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.errorText)?.visibility = View.VISIBLE
            }

        }
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

