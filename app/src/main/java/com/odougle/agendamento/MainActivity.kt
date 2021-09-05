package com.odougle.agendamento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.odougle.agendamento.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private val binding :  ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val wm = WorkManager.getInstance(this)
    private var workId: UUID? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnOneTime.setOnClickListener {
            val input = Data.Builder()
                .putString(MyWork.PARAM_FIRST_NAME, "Douglas")
                .build()

            val request = OneTimeWorkRequest.Builder(MyWork::class.java)
                .setInputData(input)
                .build()

            observeAndEnqueue(request)
        }

        binding.btnStop.setOnClickListener {
            workId?.let { uuid ->
                wm.cancelWorkById(uuid)
            }
        }

    }

    private fun observeAndEnqueue(request: OneTimeWorkRequest) {

    }
}