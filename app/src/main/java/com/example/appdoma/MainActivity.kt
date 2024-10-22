package com.example.appdoma

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity(){

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLer: Button = findViewById(R.id.button_ler)

        if (!isNotificationServiceEnabled()){
            AlertDialog.Builder(this)
                .setTitle("Autorização necessária")
                .setMessage("Permita a leitura de notificação")
                .setPositiveButton("OK"){_, _ ->
                    val intent = Intent(Settings.ACTION_SETTINGS)
                    startActivity(intent)
                }
        }

        tts = TextToSpeech(this){
            status ->
            if(status == TextToSpeech.SUCCESS){
                tts.language = Locale("pt","PT")

            }
        }

        buttonLer.setOnClickListener{
            val notifications = NotificationListener.notificationsText

            if(notifications.isNotEmpty()){
                tts.speak(notifications, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                tts.speak("Nenhuma notificação disponível", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized){
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val flat = Settings.Secure.getString(contentResolver, "enabled_notifiaction_listeners")
        return flat != null && flat.contains((packageName))
    }
}



