package com.nuang.myfirstapp


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import java.security.MessageDigest


class LoginActivity : AppCompatActivity() {
    val data = Intent()

    val callbackManager = CallbackManager.Factory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppEventsLogger.activateApp(application)
        val loginButton = findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions("email")
        Log.d("aaaa>", "aaa")


        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) { // App code
                Log.d("logged in>>", "asd")
                val request = GraphRequest.newMeRequest(loginResult?.accessToken) {`object`, response ->
                    try {
                        Log.d("FBLOGIN>>", `object`.toString())
                        if (`object`.has("id")) {
                            data.putExtra("userdata", `object`.toString())
                            setResult(Activity.RESULT_OK, data)
                            finish()
                        }
                    }
                    catch (e: Exception) {
                        Log.d("LoginException>>", e.toString())
                    }
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() { // App code
                Log.d("logged in>>", "canceled")

            }

            override fun onError(exception: FacebookException) { // App code
                Log.d("logged in>>", exception.toString())
            }
        })
    }


    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}