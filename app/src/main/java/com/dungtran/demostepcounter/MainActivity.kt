package com.dungtran.demostepcounter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dungtran.demostepcounter.databinding.ActivityMainBinding
import java.lang.Math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding

    private var sensorManager: SensorManager? = null
    private lateinit var sensor: Sensor
    private var running = false
    private var magnitudePrevious = 0.0
    private var stepCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        resetStep()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onResume() {
        super.onResume()
        running = true
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val magnitude = kotlin.math.sqrt(x * x + y * y + z * z)
            val magnitudeDelta = magnitude - magnitudePrevious
            magnitudePrevious = magnitude.toDouble()

            if (magnitudeDelta > 7) stepCount++
            binding.tvStepCounter.text = (""+stepCount)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private fun resetStep() {
        binding.btnReset.setOnClickListener {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_LONG).show()
            Log.d("main", "resetStep: tap")
        }
        binding.btnReset.setOnLongClickListener {
            stepCount = 0
            binding.tvStepCounter.text = "0"
            saveData()
            true
        }
    }

    private  fun saveData() {
        val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("primaryKey", stepCount)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getInt("primaryKey", 0)
        stepCount = savedNumber
    }

}