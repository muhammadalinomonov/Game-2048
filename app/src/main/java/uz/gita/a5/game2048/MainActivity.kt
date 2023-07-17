package uz.gita.a5.game2048

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import uz.gita.a5.game2048.repository.AppRepository

class MainActivity : AppCompatActivity() {
    private lateinit var btnRecord:CardView
    private val repository = AppRepository.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)*/
        /*btnRecord = findViewById(R.id.btn_record)*/

        findViewById<CardView>(R.id.btn_play).setOnClickListener {
            startActivity(Intent(this, PlayActivity::class.java))
        }
        findViewById<CardView>(R.id.btn_quit).setOnClickListener {
            finish()
        }

        /*btnRecord.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.record_dialog)

            dialog.setCancelable(false)
            dialog.findViewById<Button>(R.id.home).setOnClickListener{
                dialog.dismiss()
            }
            dialog.findViewById<TextView>(R.id.txt_first).text = repository.getRecord().toString()
            dialog.findViewById<TextView>(R.id.txt_second).text = repository.getRecord2().toString()
            dialog.findViewById<TextView>(R.id.txt_third).text = repository.getRecord3().toString()
            dialog.setCancelable(false)
            dialog.show()
        }*/


    }

}