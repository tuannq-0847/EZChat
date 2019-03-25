package com.sun.chat_04.presenter

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.provider.ContactsContract.Data
import android.util.Log
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sun.chat_04.model.Users
import com.sun.chat_04.util.Util

class LoginPresenter(private val context: Context, private val auth: FirebaseAuth) {
    private var util: Util = Util()

    private companion object {
        const val TAG = "LoginPresenter"
    }

    fun handlerFacebookLogin(token: AccessToken?) {
        Log.d(TAG, token?.token.toString())
        val credential = token?.token?.let { FacebookAuthProvider.getCredential(it) }
        credential?.let {
            auth.signInWithCredential(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser: FirebaseUser? = auth.currentUser
                    util.saveUserToDb(currentUser)
                } else {
                    Log.d(TAG, task.exception.toString())
                }
            }
        }
    }
}