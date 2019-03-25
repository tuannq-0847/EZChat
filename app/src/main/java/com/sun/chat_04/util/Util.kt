package com.sun.chat_04.util

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sun.chat_04.model.Users

class Util {
    private val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val mDatabaseRef: DatabaseReference = mFirebaseDatabase.reference

    companion object {
        const val TAG = "Util"
    }

    fun saveUserToDb(currentUser: FirebaseUser?) {
        Log.d(TAG, currentUser?.displayName.toString())
        val user = Users(
            currentUser?.uid, currentUser?.displayName, null, null, null, null,
            null, Constants.ONLINE
        )
        mDatabaseRef.child("Users").child(currentUser?.uid.toString())
            .setValue(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "save successfully")
                } else {
                    Log.d(TAG, "save failed : ${task.exception}")
                }
            }
    }
}