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

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var binding: ActivityMainBinding

    private var sensorManager: SensorManager? = null
    private var running = false
    private var totalStep = 0f
    private var previousTotalSteps = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadData()
        resetStep()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor != null) {
            sensorManager!!.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("main", "onSensorChanged: $totalStep")
        if (running) {
//            totalStep = event!!.values[0]
//            val currentStep = totalStep.toInt() - previousTotalSteps.toInt()
            binding.tvStepCounter.text = ("" + event!!.values[0])//("$currentStep")
        }
        Toast.makeText(this, "Sensor change", Toast.LENGTH_SHORT).show()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private fun resetStep() {
        binding.btnReset.setOnClickListener {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_LONG).show()
            Log.d("main", "resetStep: tap")
        }
        binding.btnReset.setOnLongClickListener {
            previousTotalSteps = totalStep
            Log.d("main", "resetStep: $totalStep")
            binding.tvStepCounter.text = "120"
            saveData()
            true
        }
    }

    private  fun saveData() {
        val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("primaryKey", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPre", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("primaryKey", 0f)
        previousTotalSteps = savedNumber

        Log.d("Main", "loadData: 000000000000")
    }
}