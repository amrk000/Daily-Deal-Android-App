package com.amrk000.dailydeal.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException


object ImageBase64Converter {
    //My Encoder Config
    private val centerCrop = true
    private val imageWidth = 500
    private val imageHeight = 500
    private val imageQuality = 75
    private val imageFormat = CompressFormat.JPEG
    private val metaData = "data:image/jpeg;base64,"

    fun encode(drawable: Drawable, withMetaData: Boolean): String {
        var bitmapDrawable = try {
            drawable as BitmapDrawable
        } catch (e: ClassCastException) {
            (drawable as TransitionDrawable).getDrawable(1) as BitmapDrawable
        }

        var bitmap = bitmapDrawable.bitmap

        val byteArrayOutputStream = ByteArrayOutputStream()

        if (centerCrop) {
            bitmap = if (bitmap.width > bitmap.height) Bitmap.createBitmap(
                bitmap,
                bitmap.width / 2 - bitmap.height / 2,
                0,
                bitmap.height,
                bitmap.height
            )
            else Bitmap.createBitmap(
                bitmap,
                0,
                bitmap.height / 2 - bitmap.width / 2,
                bitmap.width,
                bitmap.width
            )
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, false)

        bitmap.compress(imageFormat, imageQuality, byteArrayOutputStream)

        val image: String =
            Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)

        if(withMetaData) return metaData + image
        else return image
    }

    fun encode(context: Context, uri: Uri?, withMetaData: Boolean): String {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val byteArrayOutputStream = ByteArrayOutputStream()

        if (centerCrop) {
            bitmap = if (bitmap!!.width > bitmap.height) Bitmap.createBitmap(
                bitmap, bitmap.width / 2 - bitmap.height / 2, 0, bitmap.height, bitmap.height
            )
            else Bitmap.createBitmap(
                bitmap,
                0,
                bitmap.height / 2 - bitmap.width / 2,
                bitmap.width,
                bitmap.width
            )
        }

        bitmap = Bitmap.createScaledBitmap(bitmap!!, imageWidth, imageHeight, false)

        bitmap.compress(imageFormat, imageQuality, byteArrayOutputStream)

        val image: String =
            Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.NO_WRAP)

        if(withMetaData) return metaData + image
        else return image
    }

    fun decode(context: Context, base64: String): Drawable {
        val image = base64.substringAfter(",")
        val imageBytes = Base64.decode(image, Base64.DEFAULT)
        val bitmapDrawable = BitmapDrawable(context.resources, BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size))
        return bitmapDrawable
    }

}