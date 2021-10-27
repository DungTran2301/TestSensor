package com.dungtran.demostepcounter

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dungtran.demostepcounter.databinding.ActivityMainBinding
import java.util.jar.Manifest
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var sensorManager: SensorManager

    private lateinit var sensor: Sensor
    private var running = false
    private var magnitudePrevious = 0.0
    private var stepCount = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("Main", "onCreate: test onCreate work")

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    }

    override fun onSensorChanged(event: SensorEvent) {
        Toast.makeText(this, "In sensor change", Toast.LENGTH_LONG).show()

        Log.d("Main", "onSensorChanged: sensor is changing")
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val magnitude = sqrt(x*x + y*y +z*z)
        val magnitudeDelta = magnitude - magnitudePrevious
        magnitudePrevious = magnitude.toDouble()

        if (magnitudeDelta > 6) stepCount++
        binding.tvStepCounter.text = (""+stepCount)
    }
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)

    }



    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


}