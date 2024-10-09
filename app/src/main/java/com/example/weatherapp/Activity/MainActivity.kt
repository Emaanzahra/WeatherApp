package com.example.weatherapp.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View

import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.Adapter.ForecastAdapter
import com.example.weatherapp.Model.CurrentResponseApi
import com.example.weatherapp.Model.ForecastResponseApi
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.WeatherViewModel
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel: WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Correct usage for window flag and status bar color
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }

        binding.apply {
            var lat = intent.getDoubleExtra("lat", 0.0)
            var lon = intent.getDoubleExtra("lon", 0.0)
            var name = intent.getStringExtra("name")

            if (lat == 0.0) {
                lat = 51.50
                lon = -0.12
                name = "London"
            }

            addCityImage.setOnClickListener {
                startActivity(Intent(this@MainActivity, City::class.java))
            }

            cityTxt.text = name
            progressBar.visibility = View.VISIBLE

            // Load current weather
            weatherViewModel.loadCurrentWeather(lat, lon, "metric").enqueue(object : Callback<CurrentResponseApi> {
                override fun onResponse(call: Call<CurrentResponseApi>, response: Response<CurrentResponseApi>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        progressBar.visibility = View.GONE
                        detailLayout.visibility = View.VISIBLE
                        data?.let {
                            statusTxt.text = it.weather?.get(0)?.main ?: "-"
                            windTxt.text = it.wind?.speed?.let { speed -> Math.round(speed).toString() } ?: "-"
                            humidityTxt.text = it.main?.humidity?.toString() + "%"
                            currentTempTxt.text = it.main?.temp?.let { temp -> Math.round(temp).toString() } + "°"
                            maxTempTxt.text = it.main?.tempMax?.let { tempMax -> Math.round(tempMax).toString() } + "°"
                            minTempTxt.text = it.main?.tempMin?.let { tempMin -> Math.round(tempMin).toString() } + "°"

                            val drawable = if (isNight()) R.drawable.night_bg else R.drawable.night_bg
                            BGImage.setImageResource(drawable)

                            val icon = it.weather?.get(0)?.icon ?: "-"
                            setEffectRainSnow(icon)
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CurrentResponseApi>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })

            //forecast weather
            weatherViewModel.loadForecastWeather(lat,lon,"metric").enqueue(object : Callback<ForecastResponseApi>{
                override fun onResponse(
                    call: Call<ForecastResponseApi>,
                    response: Response<ForecastResponseApi>
                ) {
                    if(response.isSuccessful){
                        val data = response.body()
                        data?.let {
                            forecastAdapter.differ.submitList(it.list)
                            foreCastView.apply {
                                layoutManager=LinearLayoutManager(this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false)
                                adapter=forecastAdapter
                            }

                        }
                    }
                }


                override fun onFailure(call: Call<ForecastResponseApi>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun isNight(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 19
    }

    private fun setEffectRainSnow(icon: String): Int {
        return when (icon.dropLast(1)) {
            "01" -> R.drawable.snow_bg
            "02", "03", "04" -> R.drawable.cloudy_bg
            "09", "10", "11" -> R.drawable.rainy_bg
            "13" -> R.drawable.snow_bg
            "50" -> R.drawable.haze_bg
            else -> 0
        }
    }
}
