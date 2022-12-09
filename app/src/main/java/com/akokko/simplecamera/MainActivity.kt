package com.akokko.simplecamera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Objects

class MainActivity : AppCompatActivity() {

    private val takePhoto: Button by lazy { findViewById(R.id.btn_camera) }
    private val openAlbum: Button by lazy { findViewById(R.id.btn_album) }
    private val imageView: ImageView by lazy { findViewById(R.id.imageView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(this), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onStart() {
        super.onStart()
        takePhoto.setOnClickListener {
            val intent = Intent()
            intent.action = "android.media.action.STILL_IMAGE_CAMERA"
            startActivity(intent)
        }
        openAlbum.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, null)
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        val image = data.data
        val filePath = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(image!!, filePath, null, null, null)
        cursor!!.moveToFirst()
        val index = cursor.getColumnIndex(filePath[0])
        val path = cursor.getString(index)
        cursor.close()
        imageView.setImageBitmap(BitmapFactory.decodeFile(path))
    }
}