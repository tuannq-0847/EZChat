package com.sun.chat_04.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sun.chat_04.R.id
import com.sun.chat_04.R.layout
import com.sun.chat_04.R.string
import com.sun.chat_04.presenter.LoginPresenter
import com.sun.chat_04.util.Constants
import kotlinx.android.synthetic.main.activity_main.btn_login_fb
import kotlinx.android.synthetic.main.activity_main.button_login_face
import kotlinx.android.synthetic.main.activity_main.button_login
import kotlinx.android.synthetic.main.activity_main.text_email_login
import kotlinx.android.synthetic.main.activity_main.text_pass_login

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var builder: AlertDialog.Builder
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var loginPre: LoginPresenter
    private var email = ""
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        initComponents()
        initLoginFacebook()
        loginPre = LoginPresenter(this, auth)
        button_login_face.setOnClickListener(this)
        button_login.setOnClickListener(this)
    }

    private fun initComponents() {
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseRef = firebaseDatabase.getReference(Constants.USERS)
        val handler = Handler()
        runOnUiThread(object : Runnable {
            override fun run() {

                if (text_email_login.text.isNotEmpty() && text_pass_login.text.isNotEmpty()) {
                    button_login.setBackgroundColor(Color.parseColor(Constants.COLOR_BTN_LOGIN))
                    button_login.isClickable = true
                } else {
                    button_login.isClickable = false
                    button_login.setBackgroundColor(Color.parseColor(Constants.COLOR_BTN_LOGIN_LIGHT))
                }
                handler.postDelayed(this, Constants.ONE_SECOND)
            }
        })

        builder = AlertDialog.Builder(this)
        builder.setTitle(string.login_failed_title)
            .setMessage(string.login_failed)
            .setPositiveButton(string.login_failed_ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            id.button_login_face -> {
                btn_login_fb.performClick()
            }
            id.button_login -> {
                loginWithEmailAndPass()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun loginWithEmailAndPass() {
        if (text_email_login.text.isNotEmpty() && text_pass_login.text.isNotEmpty()) {
            email = text_email_login.text.toString()
            pass = text_pass_login.text.toString()
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "successfully...")
                    } else {
                        builder.show()
                        Log.d(TAG, "onFailed...")
                    }
                }
        }
    }

    private fun initLoginFacebook() {
        callbackManager = CallbackManager.Factory.create()
        btn_login_fb.setReadPermissions("email", "public_profile")
        btn_login_fb.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                loginPre.handlerFacebookLogin(result?.accessToken)
            }

            override fun onCancel() {
                Log.d(TAG, "onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d(TAG, "onError: $error")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
