package com.odougle.agendamento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
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
        wm.enqueue(request)
        workId = request.id
        wm.getWorkInfoByIdLiveData(request.id).observe(this, androidx.lifecycle.Observer { status ->
            binding.txtStatus.text = when (status?.state){
                WorkInfo.State.ENQUEUED -> "Enfileirado"
                WorkInfo.State.BLOCKED -> "Bloqueado"
                WorkInfo.State.CANCELLED -> "Cancelado"
                WorkInfo.State.RUNNING -> "Executando"
                WorkInfo.State.SUCCEEDED -> "Sucesso"
                WorkInfo.State.FAILED -> "Falhou"
                else -> "Indefinido"
            }
            binding.txtOutput.text = status?.outputData?.run {
                """${getString(MyWork.PARAM_NAME)}
                    |${getInt(MyWork.PARAM_AGE,0)}
                    |${getLong(MyWork.PARAM_TIME,0)}
                """.trimMargin()
            }
        })
    }
}