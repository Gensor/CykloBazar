package com.gensor.cyklobazar.activities.Ad


class ViewVisibilityManager {
    private val layouts : ArrayList<ViewVisibility> = ArrayList()

    var visibleView : ViewVisibility? = null
        set(value) {
            field = value
            notifyViews()
        }

    fun attach(viewVisibility : ViewVisibility){
        this.layouts.add(viewVisibility)
    }

    private fun notifyViews() {
        for (viewVisibility in layouts){
            viewVisibility.update()
        }
    }

}