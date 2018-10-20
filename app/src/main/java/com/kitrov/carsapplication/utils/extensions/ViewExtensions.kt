package com.kitrov.carsapplication.utils.extensions

import android.content.ContextWrapper
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context
    while (context is ContextWrapper) {
        if (context is AppCompatActivity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun TextView.setTextWithLabel(title: String, labelResource: Int) {
    text = String.format(resources.getString(labelResource), title)
}