package com.nuang.myfirstapp


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_third.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ThirdFragment : Fragment() {

    var claimerList = arrayListOf<ClaimItem>()
    var claimeeList = arrayListOf<ClaimItem>()

    val serverUrl = "http://34.84.158.57:4001"
    var uid: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ClaimerAdapter = MoneyAdapter(requireContext(), claimerList)
        claimerRecyclerView.adapter = ClaimerAdapter
        val ClaimeeAdapter = MoneyAdapter(requireContext(), claimeeList)
        claimeeRecyclerView.adapter = ClaimeeAdapter

        val claimerLayoutManager = LinearLayoutManager(requireContext())
        claimerRecyclerView.layoutManager = claimerLayoutManager
        claimerRecyclerView.setHasFixedSize(true)
        val claimeeLayoutManager = LinearLayoutManager(requireContext())
        claimeeRecyclerView.layoutManager = claimeeLayoutManager
        claimeeRecyclerView.setHasFixedSize(true)

        claimee_button.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.add_claimee_pop_up, null)
            val claimee_id = dialogView.findViewById<EditText>(R.id.popup_claimee_id)
            val name = dialogView.findViewById<EditText>(R.id.popup_claimee_name)
            val amount = dialogView.findViewById<EditText>(R.id.popup_claimee_amount)

            builder.setView(dialogView)
                .setPositiveButton("확인") {
                    dialogInterface, i ->
                        //val _id =
                        val claimeeId = claimee_id.text.toString()
                        val transactionName = name.text.toString()
                        val cash = Integer.parseInt(amount.text.toString())
                        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                        val date = sdf.format(Date()).toString()
                        val claimee = ClaimItem(_id="", claimId=claimeeId, name=transactionName, amount=cash, date=date, account="")
                        claimeeList.add(claimee)
                }
                .setNegativeButton("취소") {
                        dialogInterface, i ->

                }
                .show()
        }
        claimer_button.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.add_claimer_pop_up, null)
            val claimer_id = dialogView.findViewById<EditText>(R.id.popup_claimer_id)
            val name = dialogView.findViewById<EditText>(R.id.popup_claimer_name)
            val amount = dialogView.findViewById<EditText>(R.id.popup_claimer_amount)
            val account = dialogView.findViewById<EditText>(R.id.popup_claimer_account)

            builder.setView(dialogView)
                .setPositiveButton("확인") {
                    dialogInterface, i ->
                        //val _id =
                        val claimerId = claimer_id.text.toString()
                        val transactionName = name.text.toString()
                        val cash = Integer.parseInt(amount.text.toString())
                        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                        val date = sdf.format(Date()).toString()
                        val account_t = account.text.toString()
                        val claimer = ClaimItem(_id="0", claimId=claimerId, name=transactionName, amount=cash, date=date, account=account_t)
                        claimerList.add(claimer)
                        ClaimerAdapter.notifyDataSetChanged()
                }
                .setNegativeButton("취소") {
                        dialogInterface, i ->

                }
                .show()
        }
    }

    /*
    val weatherAPI: String = "e2054e5fd85d82fb049fe2c791bfe058"
    val fineDustAPI: String = "uo81tqCqaqN2cI45bKhC8%2BFOaEg6hvoiCxLRIDx1Ks4vPqUmHUHovDuiJJanHpbYEUvnCt2U4BdcoKgsUswjkQ%3D%3D"
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    val cityNames = arrayOf(
        "서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남",
        "전북", "전남", "경북", "경남", "제주", "세종"
    )
    var carrot_size = 0
    var sunFlag = false

    fun setBackGround() {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        val water = view.findViewById(R.id.water) as ImageView
        water.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                carrot_size = carrot_size + 1
            }
        })
        val sun = view.findViewById(R.id.sun) as ImageView
        sun.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                sunFlag = true
            }
        })
        val carrot = view.findViewById(R.id.carrot) as ImageView
        carrot.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if(3<=carrot_size && sunFlag) {
                    carrot_size = 0
                    sunFlag = false
                    val intent = Intent(view.context, ys_egg::class.java)
                    activity?.startActivity(intent)
                }
            }
        })

        val textviewAddress = view.findViewById(R.id.address) as TextView
        //val ShowLocationButton = view.findViewById(R.id.button) as Button
        //ShowLocationButton.setOnClickListener(View.OnClickListener {
        val gpsTracker = GpsTracker(context)

        latitude = gpsTracker.latitude
        longitude = gpsTracker.longitude

        val address = getCurrentAddress(latitude, longitude)
        Log.d("address >>", address)

        val addressParsed = address.split("\\s".toRegex())
        textviewAddress.text = addressParsed[1] + " " + addressParsed[2]
        val citySubName = addressParsed[2]

        for(city in cityNames) {
            Log.d("cityname>>>", city)
            if (city in addressParsed[1]) {
                val cityName = city
                Log.d("parsed >>", cityName + "  " + citySubName)
                fineDustTask(cityName, citySubName).execute()
                break
            }
        }
        weatherTask().execute()
        return view
    }

    private fun getCurrentAddress(latitude: Double, longitude: Double): String {
        //convert latitude&longitude into address with geocoder
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>?
        try {
            addresses = geocoder.getFromLocation(
                latitude,
                longitude,
                7
            )
        } catch (ioException: IOException) {
            //Network Error
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
    inner class weatherTask : AsyncTask<String, Void, String>() {
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
                response = URL("https://api.openweathermap.org/data/2.5/weather" +
                        "?lat=$latitude" +
                        "&lon=$longitude" +
                        "&units=metric" +
                        "&appid=$weatherAPI").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
                Log.d("noResponse>>", "in weatherTask")
            }
            return response
        }

        //colorscheme from : https://codepen.io/bork/pen/wJhEm, sunrise = 7~(index: 0~), sunset = 19~(index: 12~)
        val colorPaletteTop = arrayOf("#12a1c0", "#74d4cc", "#efeebc", "#fee154", "#fdc352", "#ffac6f", "#fda65a", "#fd9e58",  "#f18448", "#f06b7e", "#ca5a92", "#5b2c83",
            "#371a79", "#28166b", "#192861", "#040b3c", "#040b3c", "#012459", "#003972", "#003972", "#004372", "#004372", "#016792", "#07729f")
        val colorPaletteBottom = arrayOf("#07506e", "#1386a6", "#61d0cf", "#a3dec6", "#e8ed92", "#ffe467", "#ffe467", "#ffe467", "#ffd364", "#f9a856", "#f4896b", "#d1628b",
            "#713684", "#45217c", "#372074", "#233072", "#012459", "#001322", "#001322", "#001322", "#00182b", "#011d34", "#00182b", "#042c47")

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

                val sunriseTomorrow = sunrise + 86400
                val dayTime = sunset - sunrise
                val nightTime = sunriseTomorrow - sunset
                val currentTime = System.currentTimeMillis() / 1000


                Log.d("times : ", "$currentTime, $updatedAt, $sunset, $sunrise")
                val index = if (currentTime >= sunset) {
                    //nightTime
                    (((currentTime - sunset) * 12) / (nightTime)).toInt() + 12
                    //Log.d("index calculated : ", "(($currentTime - $sunset) * 12) / $nightTime + 12)")
                } else {
                    (((currentTime - sunrise) * 12) / (dayTime)).toInt()
                    //Log.d("index calculated : ", "(($currentTime - $sunrise) * 12) / $dayTime)")
                }
                Log.d("index is :", index.toString())
                val colors = intArrayOf(Color.parseColor(colorPaletteTop[index]), Color.parseColor(colorPaletteBottom[index]))
                val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
                view?.findViewById<FrameLayout>(R.id.backGround)?.background = gradientDrawable

                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                jsonObj.getString("name")+", "+sys.getString("country")

                /* Populating extracted data into our views */
                //view?.findViewById<TextView>(R.id.address)?.text = address
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
                view?.findViewById<ConstraintLayout>(R.id.mainContainer)?.visibility = View.VISIBLE

            } catch (e: Exception) {
                Log.d("exception>>", e.toString())
                view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.errorText)?.visibility = View.VISIBLE
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    inner class fineDustTask(val cityName: String, val citySubName: String) : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.VISIBLE
            view?.findViewById<RelativeLayout>(R.id.mainContainer)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.errorText)?.visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response = URL(
                    "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst" +
                            "?ServiceKey=$fineDustAPI" +
                            "&numOfRows=100" +
                            "&pageNo=1" +
                            "&sidoName=$cityName" +
                            "&searchCondition=HOUR" +
                            "&_returnType=json"
                ).readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception) {
                response = null
                Log.d("noResponse>>", "in fineDust")
            }
            return response
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.CUPCAKE)
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                Log.d("result>>", result)
                val jsonObj = JSONObject(result)
                val infoArray = jsonObj.getJSONArray("list")
                Log.d("infoarray>>", infoArray.length().toString())
                for (i in 0..infoArray.length()) {
                    Log.d("entered loop>>", "asdf")
                    val tempObject = infoArray.getJSONObject(i)
                    val tempString = tempObject.getString("cityName")
                    Log.d("tempstring>>", tempString + "citysubname: " + citySubName)
                    if (tempString in citySubName) {
                        val pm10Value = tempObject.getString("pm10Value")
                        val pm25Value = tempObject.getString("pm25Value")
                        Log.d("value>>", pm10Value + "  " + pm25Value)
                        view?.findViewById<TextView>(R.id.pm10Value)?.text = "$pm10Value㎍/m³"
                        view?.findViewById<TextView>(R.id.pm25Value)?.text = "$pm25Value㎍/m³"

                        when {
                            pm10Value.toInt() <= 30 -> view?.findViewById<ImageView>(R.id.imageView)?.setImageResource(R.drawable.ic_happy)
                            pm10Value.toInt() <= 80 -> view?.findViewById<ImageView>(R.id.imageView)?.setImageResource(R.drawable.ic_fine)
                            pm10Value.toInt() <= 150 -> view?.findViewById<ImageView>(R.id.imageView)
                                ?.setImageResource(R.drawable.ic_angry)
                            else -> view?.findViewById<ImageView>(R.id.imageView)?.setImageResource(R.drawable.ic_devil)
                        }

                        when {
                            pm25Value.toInt() <= 15 -> view?.findViewById<ImageView>(R.id.imageView2)?.setImageResource(R.drawable.ic_happy)
                            pm25Value.toInt() <= 35 -> view?.findViewById<ImageView>(R.id.imageView2)?.setImageResource(R.drawable.ic_fine)
                            pm25Value.toInt() <= 75 -> view?.findViewById<ImageView>(R.id.imageView2)
                                ?.setImageResource(R.drawable.ic_angry)
                            else -> view?.findViewById<ImageView>(R.id.imageView2)?.setImageResource(R.drawable.ic_devil)
                        }


                        break
                    }
                }
            } catch (e: Exception) {
                Log.d("exception>>", "exception???")
                view?.findViewById<ProgressBar>(R.id.loader)?.visibility = View.GONE
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

     */
}

