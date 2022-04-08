package com.gensor.cyklobazar.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

object Constants {

    //database
    const val USERS: String = "users"
    const val USER_IMAGE : String = "image"
    const val USER_NAME : String = "name"
    const val USER_ID : String = "id"
    const val USER_EMAIL : String = "email"
    const val EBIKE : String = "ebike"
    const val MOUNTAINBIKE: String = "mountainBike"
    const val ROADBIKE : String = "roadBike"
    const val FORK : String = "fork"
    const val WHEEL : String = "wheel"
    const val PRODUCT_USER_ID = "userId"

    //storage
    const val PROFILE_PICTURES : String = "profile_photos/"
    const val PRODUCT_IMAGES : String = "product_images/"

    //permisions
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_CODE = 2

    fun reduceImageSize(context: Context, uri: Uri): ByteArray{
        val bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
        val reducedImage: ByteArray = byteArrayOutputStream.toByteArray()

        return reducedImage
    }

}