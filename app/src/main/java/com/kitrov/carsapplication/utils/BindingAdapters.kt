package com.kitrov.carsapplication.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.kitrov.carsapplication.R
import com.kitrov.carsapplication.utils.extensions.getParentActivity
import com.kitrov.carsapplication.utils.extensions.setTextWithLabel
import com.squareup.picasso.Picasso

@BindingAdapter("mutableVisibility")
fun setMutableVisibility(view: View, visibility: MutableLiveData<Int>?) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null && visibility != null) {
        visibility.observe(parentActivity, Observer { value -> view.visibility = value ?: View.VISIBLE })
    }
}

@BindingAdapter("text")
fun setText(view: TextView, text: String) {
    view.text = text
}

@BindingAdapter("adapter")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("imageFromUrl")
fun setImageFromUrl(view: ImageView, photoUrl: String) {
    Picasso.get().load(photoUrl).into(view)
}

@BindingAdapter("plateNumber")
fun setPlateNumber(view: TextView, plateNumber: String) {
    view.setTextWithLabel(plateNumber, R.string.plate_number_label)
}

@BindingAdapter("batteryPercentage")
fun setBatteryPercentage(view: TextView, batteryPercentage: MutableLiveData<Int>) {
    val parentActivity: AppCompatActivity? = view.getParentActivity()
    if (parentActivity != null) {
        batteryPercentage.observe(parentActivity,
            Observer { value -> view.setTextWithLabel(value.toString(), R.string.battery_label) })
    }
}