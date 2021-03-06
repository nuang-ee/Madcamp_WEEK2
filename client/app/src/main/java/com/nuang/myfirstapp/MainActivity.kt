package com.nuang.myfirstapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.facebook.AccessToken
import com.facebook.Profile
import com.facebook.login.LoginManager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.nuang.myfirstapp.adapter.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


private const val IMAGE_PICK_CODE = 1000
val PERMISSIONS_REQUEST_CODE = 100
val LOGIN_REQUEST_CODE = 300

class MainActivity : AppCompatActivity() {
    val serverUrl = "http://34.84.158.57:4001"

    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
    var uid = ""
    lateinit var userInfo: JSONObject

    fun startApp() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById<DrawerLayout>(R.id.mLayout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout_button -> {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START)
                    }
                    LoginManager.getInstance().logOut()

                    //Set App as First Launched
                    val preferences: SharedPreferences = getSharedPreferences("com.example.myfirstapp",
                        Context.MODE_PRIVATE
                    )
                    preferences.edit().putBoolean("ifFirstRun", false).commit()

                    checkLogin()
                    true
                }
                else -> false
            }
        }

        Log.d("fuckfuck>>", "fuck")
        val fragmentAdapter =
            MyPagerAdapter(supportFragmentManager, uid)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var checkResult = true
            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }

            if (checkResult) {
                // 모든 퍼미션을 허용했다면 앱 시작.
                startApp()
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[2]))
                {
                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.
                    Snackbar.make(
                        mLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인") { finish() }.show()

                } else {
                    // “다시 묻지 않음”을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(
                        mLayout, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("확인") { finish() }.show()
                }
            }
        }
    }

    fun checkLogin() {
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired
        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java).apply {
                //add something if needed
            }
            startActivityForResult(intent, LOGIN_REQUEST_CODE)
        }
        else {
            uid = Profile.getCurrentProfile().id
            checkUser().execute()
            val imageUri = Profile.getCurrentProfile().getProfilePictureUri(128, 128)
            val navigationView = findViewById<NavigationView>(R.id.navigationView)
            val headerView = navigationView.inflateHeaderView(R.layout.nav_header)
            val imageView = headerView.findViewById<ImageView>(R.id.fb_profile_image)
            val textView = headerView.findViewById<TextView>(R.id.fb_name)
            textView.text = Profile.getCurrentProfile().name
            Glide.with(this)
                .load(imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView)
            checkRunTimePermission()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkLogin()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("asdasdkwod>>", "something's wrong")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LOGIN_REQUEST_CODE) {
                val returnedResult = data?.getStringExtra("userdata")
                if (returnedResult == null) {
                    Log.d("returnedResult>>", "is null")
                    finish()
                }
                userInfo = JSONObject(returnedResult)
                uid = userInfo.getString("id")
                Log.d("uid>>", uid)
                Log.d("LoginData>>", returnedResult)
                checkRunTimePermission()
            }
            else if (requestCode == IMAGE_PICK_CODE) {
                Log.d("data>>", "dataOnMainActivity")
                Log.d("data>>", data.toString())
            }
        }
    }

    fun checkRunTimePermission() {
        val hasReadContactsPermission = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_CONTACTS)
        val hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
        val hasInternetPermission = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.INTERNET)
        //val hasFineLocationPermission = ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)

        if (hasReadContactsPermission == PackageManager.PERMISSION_GRANTED &&
                hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
                hasInternetPermission == PackageManager.PERMISSION_GRANTED)
        {
            //Already has permission
            startApp()
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, REQUIRED_PERMISSIONS[0]) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, REQUIRED_PERMISSIONS[1]) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, REQUIRED_PERMISSIONS[2]))
                {
                Snackbar.make(
                    mLayout, "이 앱을 실행하려면 카메라와 외부 저장소, 인터넷 접근 권한이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("확인") {
                    // 3-3. 사용자에게 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions(
                        this@MainActivity, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE
                    )
                }.show()
            }
            else {
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    inner class checkUser: AsyncTask<Void, Void, String?>() {
        override fun doInBackground(vararg p0: Void?): String? {
            var result = ""
            try {
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

}