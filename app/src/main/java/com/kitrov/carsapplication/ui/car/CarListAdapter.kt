package com.kitrov.carsapplication.ui.car

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.kitrov.carsapplication.R
import com.kitrov.carsapplication.databinding.ItemCarBinding
import com.kitrov.carsapplication.model.CarInfo
import com.kitrov.carsapplication.model.entities.CarEntity

class CarListAdapter: RecyclerView.Adapter<CarListAdapter.ViewHolder>() {
    private lateinit var carList:List<CarEntity>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarListAdapter.ViewHolder {
        val binding: ItemCarBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_car, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(::carList.isInitialized) carList.size else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(carList[position])
    }

    fun updateCarList(carList:List<CarEntity>) {
        this.carList = carList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemCarBinding):RecyclerView.ViewHolder(binding.root){
        private val viewModel = CarViewModel()

        fun bind(carInfo: CarEntity) {
            viewModel.bind(carInfo)
            binding.viewModel = viewModel
        }
    }
}