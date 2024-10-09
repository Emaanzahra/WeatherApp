package com.example.weatherapp.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Model.ForecastResponseApi
import com.example.weatherapp.databinding.ForecastViewholderBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    // No need to declare `binding` here; we will pass it directly to the ViewHolder.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ForecastViewholderBinding.inflate(inflater, parent, false)
        return ViewHolder(binding) // Pass binding to ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Access the binding directly from the ViewHolder
        val data = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time = data

        val dayOfWeek = when(calendar.get(Calendar.DAY_OF_WEEK)){
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "-"
        }
        holder.binding.daynameTxt.text = dayOfWeek
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val ampm = if(hour < 12) "am" else "pm"
        val hour12 =  calendar.get(Calendar.HOUR)
        holder.binding.hourTxt.text = "$hour12$ampm"
        holder.binding.tempTxt.text = differ.currentList[position].main?.temp?.let{ Math.round(it).toString() } + "°"

        val icon = when(differ.currentList[position].weather?.get(0)?.icon.toString()){
            "01d" , "01n" -> "sunny"
            "02d" , "02n" -> "cloudy_sunny"
            "03d" , "03n" -> "cloudy_sunny"
            "04d" , "04n" -> "cloud"
            "09d" , "09n" -> "rainy"
            "10d" , "10n" -> "rainy"
            "11d" , "11n" -> "storm"
            "13d" , "13n" -> "snowy"
            "50d" , "50n" -> "windy"
            else -> "sunny"
        }
        val drawableResourceId: Int = holder.binding.root.resources.getIdentifier(
            icon, "drawable", holder.binding.root.context.packageName
        )

        holder.binding.pic.setImageResource(drawableResourceId)
    }

    override fun getItemCount() = differ.currentList.size

    inner class ViewHolder(val binding: ForecastViewholderBinding) : RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<ForecastResponseApi.WeatherData>() {
        override fun areItemsTheSame(
            oldItem: ForecastResponseApi.WeatherData,
            newItem: ForecastResponseApi.WeatherData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponseApi.WeatherData,
            newItem: ForecastResponseApi.WeatherData
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}
