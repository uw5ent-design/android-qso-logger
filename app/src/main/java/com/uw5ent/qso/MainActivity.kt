package com.uw5ent.qso

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCall = findViewById<EditText>(R.id.etCall)
        val etBand = findViewById<EditText>(R.id.etBand)
        val etMode = findViewById<EditText>(R.id.etMode)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val call = etCall.text.toString().uppercase().trim()
            val band = etBand.text.toString().trim()
            val mode = etMode.text.toString().uppercase().trim()

            if (call.isEmpty() || band.isEmpty() || mode.isEmpty()) {
                Toast.makeText(this, "Заполни все поля!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Формируем дату и время для ADIF
            val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HHmmss", Locale.getDefault())
            val currentDate = dateFormat.format(Date())
            val currentTime = timeFormat.format(Date())

            // Формат ADIF: <тег:длина>значение
            val adifRecord = "<call:${call.length}>$call " +
                    "<qso_date:8>$currentDate " +
                    "<time_on:6>$currentTime " +
                    "<band:${band.length}>$band " +
                    "<mode:${mode.length}>$mode " +
                    "<eor>\n"

            // Сохраняем в файл (во внутреннюю память приложения)
            try {
                val file = File(filesDir, "my_log.adi")
                file.appendText(adifRecord)
                Toast.makeText(this, "Связь сохранена в ADIF!", Toast.LENGTH_SHORT).show()
                
                // Очищаем поля для следующей связи
                etCall.text.clear()
                etMode.text.clear()
            } catch (e: Exception) {
                Toast.makeText(this, "Ошибка сохранения: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
