package com.example.twitterdemo

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_login_activty.*


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setContentView(R.layout.activity_login_activty)

        ivImagePerson.setOnClickListener(View.OnClickListener {
            checkPermission()
        })
    }
    val READIMAGE:Int = 253
    fun checkPermission()
    {
        if(Build.VERSION.SDK_INT>=23)
        {
            if(ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )!= PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    READIMAGE
                )
                return
            }
        }
        loadImage()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        when(requestCode)
        {
            READIMAGE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadImage()
                } else {
                    Toast.makeText(applicationContext, "Cannot access the image", Toast.LENGTH_LONG)
                        .show()
                }
            }
            else->super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }
    val PICK_COODE : Int = 123
    fun loadImage() {
        var intent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(intent, PICK_COODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_COODE && data != null)
        {
            val selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            ivImagePerson.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }
    fun buLogin(view: View) {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        signupToFirebase(email, password)
    }

    private fun signupToFirebase(email: String, password: String)
    {
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(applicationContext,"Please enter your email",Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(applicationContext,"Please enter password",Toast.LENGTH_LONG).show()
            return
        }
        
    }
}