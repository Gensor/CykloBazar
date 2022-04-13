package com.gensor.cyklobazar.activities.Ad

import android.view.View

class ViewVisibility (
    var view : View,
    var viewManager : ViewVisibilityManager
)
{
    init {
        viewManager.attach(this)
    }

    fun update(){
        when(viewManager.visibleView){
            this -> view.visibility = View.VISIBLE
            else -> view.visibility = View.GONE
        }
    }
}