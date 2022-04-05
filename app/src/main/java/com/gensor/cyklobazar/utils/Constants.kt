package com.gensor.cyklobazar.utils

object Constants {

    //database
    const val USERS: String = "users"
    const val USER_IMAGE : String = "image"
    const val USER_NAME : String = "name"
    const val USER_EMAIL : String = "email"
    const val EBIKE : String = "ebike"
    const val MOUNTAINBIKE: String = "mountainBike"
    const val ROADBIKE : String = "roadBike"
    const val FORK : String = "fork"
    const val WHEEL : String = "wheel"

    //storage
    const val PROFILE_PICTURES : String = "profile_photos/"

    //permisions
    const val READ_STORAGE_PERMISSION_CODE = 1
    const val PICK_IMAGE_CODE = 2



    /*
    získanie prípony súboru
     */
    /*fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }*/
}