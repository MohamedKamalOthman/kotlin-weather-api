package com.example.weather

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weather.ui.theme.WeatherTheme
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

class MainActivity : ComponentActivity() {
    lateinit var tvCity: TextView
    lateinit var tvTemp: TextView
    lateinit var tvCondition: TextView
    lateinit var ivCondition: ImageView
    var weather: Weather? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvCity = findViewById(R.id.tv_city)
        tvTemp = findViewById(R.id.tv_temp)
        tvCondition = findViewById(R.id.tv_condition)
        ivCondition = findViewById(R.id.iv_weather)

        // request params
        // get api key from grade.properties
        val apiKey = BuildConfig.API_KEY
        Log.d("API_KEY", apiKey)
        var location = "London"
        val includeAqi = "no"
        val forecastDays = 14
        val spinner = findViewById<Spinner>(R.id.city_spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                location = parent.getItemAtPosition(position).toString()
                weather = null
                CoroutineScope(Dispatchers.IO).launch {
                    weather =
                        WeatherApi.retrofitService.getWeather(
                            apiKey,
                            location,
                            includeAqi,
                            forecastDays
                        )
                }
                while (weather == null) {
                    sleep(500)
                }
                update()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            weather =
                WeatherApi.retrofitService.getWeather(apiKey, location, includeAqi, forecastDays)
        }
        while (weather == null) {
            sleep(500)
        }
        update()
    }

    private fun update() {
        val uri = Uri.parse("https:" + weather!!.current.condition.icon)

        Picasso.with(this).load(uri).resize(250, 250).into(ivCondition)

        tvCity.text = weather!!.location.name
        tvTemp.text = weather!!.current.temp_c.toInt().toString() + "°C"
        tvCondition.text = weather!!.current.condition.text
    }
}
