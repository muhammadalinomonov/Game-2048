package uz.gita.a5.game2048

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import uz.gita.a5.game2048.model.SideEnum
import uz.gita.a5.game2048.repository.AppRepository
import uz.gita.a5.game2048.settings.Settings
import uz.gita.a5.game2048.utils.BackgroundUtil
import uz.gita.a5.game2048.utils.MyTouchListener


class PlayActivity : AppCompatActivity() {

    private val items: MutableList<TextView> = ArrayList(16)
    private lateinit var mainView: LinearLayout
    private var repository = AppRepository.getInstance()
    private var settings: Settings = Settings.getInstance()
    private val util = BackgroundUtil()
    private lateinit var level: TextView
    private lateinit var record: TextView
    private var clickBackCount = 0

    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar)
        }*/
        level = findViewById(R.id.score)
        record = findViewById(R.id.record)
        mainView = findViewById(R.id.mainView)
        loadViews()
        describeMatrixToViews()




        if (clickBackCount == 1 || !repository.isPlaying()) {

            findViewById<ImageView>(R.id.back).setBackgroundResource(R.drawable.bg_for_back)
            findViewById<ImageView>(R.id.back).isClickable = false
        }

        val myTouchListener = MyTouchListener(this)
        myTouchListener.setMyMovementSideListener {

            findViewById<ImageView>(R.id.back).setBackgroundResource(R.drawable.bg_item_score)
            findViewById<ImageView>(R.id.back).isClickable = true
            clickBackCount = 0



            when (it) {

                SideEnum.DOWN -> {
                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }
                    repository.setState(true)
                    repository.moveDown()
                    describeMatrixToViews()
                }
                SideEnum.UP -> {
                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }
                    repository.setState(true)
                    repository.moveUp()
                    describeMatrixToViews()
                }
                SideEnum.RIGHT -> {
                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }
                    repository.setState(true)
                    repository.moveToRight()
                    describeMatrixToViews()
                }
                SideEnum.LEFT -> {
                    if (!repository.isClickable()) {
                        openGameOverDialog()
                    }

                    repository.setState(true)
                    repository.moveToLeft()
                    describeMatrixToViews()
                }
            }
        }
        mainView.setOnTouchListener(myTouchListener)


        findViewById<ImageView>(R.id.btn_home).setOnClickListener {
            repository.saveItems()
            finish()
        }
    }



    fun openGameOverDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View = layoutInflater.inflate(R.layout.game_over_dialog, null)

        builder.setView(view)
        val gameOverDialog = builder.create()

        view.findViewById<TextView>(R.id.txt_score).text = repository.level.toString()
        view.findViewById<Button>(R.id.btn_restart).setOnClickListener{
            repository.clear()
            gameOverDialog.dismiss()
            repository = AppRepository.getInstance()
            describeMatrixToViews()
        }
        view.findViewById<Button>(R.id.btn_home)?.setOnClickListener{
            repository.restart()
            repository = AppRepository.getInstance()
            gameOverDialog.dismiss()
            finish()
        }






        var record = record.text.toString().toInt()
        var record2 = settings.getRecord2()
        var record3 =settings.getRecord3()



        Log.d("RECORD", "$record")
        Log.d("RECORD", "$record2")
        Log.d("RECORD", "$record3")
        var level = level.text.toString().toInt()

        if(record < level){
            record3 = record2
            record2 = record
            record = level
        }
        else if(record > level && record2 < level){
            println()
            record3 = record2
            record2 = level
        }
        else if(record2 > level && record3 < level){
            record3 = level
        }


        /*if (record < level.text.toString().toInt()) {

            if (record2 != 0) {
                record3 = record2
                record2 = record
            }
            record = level.text.toString().toInt()

        } else if (record2 < level.text.toString().toInt()) {
            if (record3 != 0) {
                record3 = record2
            }
            record2 = level.text.toString().toInt()
        } else if (record3 < level.text.toString().toInt()) {
            record3 = record2
        } else if (record3 < level.text.toString().toInt()) {
            record3 = level.text.toString().toInt()
        }*/

        repository.saveRecords(record, record2,record3)



        gameOverDialog.setCancelable(false)


        gameOverDialog.show()
    }


    private fun loadViews() {


        findViewById<ImageView>(R.id.btn_refresh).setOnClickListener {
            repository.clear()
            describeMatrixToViews()
        }

        /*findViewById<ImageView>(R.id.back).setOnClickListener {
            finish()
        }*/
        for (i in 0 until mainView.childCount) {
            val linear = mainView.getChildAt(i) as LinearLayout
            for (j in 0 until linear.childCount) {
                items.add(linear.getChildAt(j) as TextView)

            }
        }
        findViewById<ImageView>(R.id.back).setOnClickListener {
            clickBackCount++
            repository.backOldState()
            describeMatrixToViews()
        }
        /*findViewById<ImageView>(R.id.button_restart).setOnClickListener{
            repository.clear()
            describeMatrixToViews()
        }*/
    }

    @SuppressLint("ResourceAsColor")
    private fun describeMatrixToViews() {
        if (clickBackCount == 1 || !repository.isPlaying()) {

            findViewById<ImageView>(R.id.back).setBackgroundResource(R.drawable.bg_for_back)
            findViewById<ImageView>(R.id.back).isClickable = false
        }
        level.text = repository.level.toString()
        record.text = repository.getRecord().toString()
        val _matrix = repository.matrix
        for (i in _matrix.indices) {
            for (j in _matrix[i].indices) {
                items[i * _matrix.size + j].apply {
                    text = if (_matrix[i][j] == 0) ""
                    else _matrix[i][j].toString()
                    if (_matrix[i][j] == 2 || _matrix[i][j] == 4) {
                        this.setTextColor(this.getContext().getColor(R.color.qoraroq));
                    } else
                        this.setTextColor(this.getContext().getColor(R.color.white));
                    setBackgroundResource(util.colorByCount(_matrix[i][j]))
//                    setTextColor(util.textColorByCount(_matrix[i][j]))
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        repository.saveItems()
    }
}