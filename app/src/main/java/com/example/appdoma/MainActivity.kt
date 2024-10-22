package com.example.appdoma

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity(){

    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLer: Button = findViewById(R.id.button_ler)
        val buttonBluetooth: ImageButton = findViewById(R.id.button_bluetooth)

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
                tts.language = Locale("pt","BR")

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

        buttonBluetooth.setOnClickListener{
            val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth não é suportado neste dispositivo", Toast.LENGTH_SHORT).show()
            } else if (!bluetoothAdapter.isEnabled) {
                Toast.makeText(this, "Conecte seu dispositivo em algum dispositivo Bluetooth", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bluetooth já está habilitado", Toast.LENGTH_SHORT).show()
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



