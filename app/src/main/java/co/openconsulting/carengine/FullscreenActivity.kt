package co.openconsulting.carengine

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import kotlinx.android.synthetic.main.activity_fullscreen.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private val updatePeriod = 500L //
    private var currentDateTime = Calendar.getInstance().getTime()
    private var currentBatteryTemp = 0
    private var currentMotorTemp = 0
    private var currentKmLeft = 120
    private var currentPowerConsumption = 4.5
    private var fakeData1 = true
    private var isBlackTheme = true


    private lateinit var updateHandler : Handler

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_fullscreen)

        supportActionBar?.hide()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mainFrameLayout.setOnClickListener {
            toggleFakeData()
        }


        updateHandler = Handler()
        updateHandler.postDelayed({
            updateUI()
        }, updatePeriod)
    }

    private fun toggleFakeData() {
        Log.d("nxqd", "here")

        if (fakeData1) {
            toggleFakeData2()
        } else {
            toggleFakeData1()
        }

        updateUI()
    }

    private fun toggleFakeData1() {
        currentBatteryTemp = 0
        currentMotorTemp = 0
        currentKmLeft = 120
        currentPowerConsumption = 4.5
        fakeData1 = true
        changeBlackTheme(false)

        kwProgressBar.progress = 50
        rocketProgressBar.progress = 50
    }

    private fun toggleFakeData2() {
        currentBatteryTemp = 90
        currentMotorTemp = 80
        currentKmLeft = 112
        currentPowerConsumption = -4.5
        fakeData1 = false
        changeBlackTheme(true)

        kwProgressBar.progress = 100
        rocketProgressBar.progress = 100
    }

    private fun currentPowerConsumptionText(): String {
        if (currentPowerConsumption > 0) {
            return "+$currentPowerConsumption ℃"
        }
        return "$currentPowerConsumption ℃"
    }

    private fun drawLine() {
        val bitmap = Bitmap.createBitmap(10, 700, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.RED)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8F
        paint.isAntiAlias = true
        val offset = 50.0F
        canvas.drawLine(
            offset, (canvas.height / 2).toFloat(), canvas.width - offset,
            (canvas.height / 2).toFloat(), paint)
        drawingImageView.setImageBitmap(bitmap)

    }

    @SuppressLint("SimpleDateFormat")
    fun dateToString(date : Date) : String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(date)
    }

    private fun tempText(temp : Int) : String {
        return "$temp ℃"
    }

    private fun isDayTime(timeOfDay: Int) : Boolean {
        return timeOfDay in 7..18
    }

    private fun updateUI() {
        val now = Calendar.getInstance()

        kmLeftTextView.text = currentKmLeft.toString()

        rocketTempTextView.text = tempText(currentBatteryTemp)
        powerConsumptionTextView.text = currentPowerConsumptionText()
    }

    private fun maybeChangeTheme(now: Calendar) {
        val timeOfDay = now.get(Calendar.HOUR_OF_DAY)
        if (isDayTime(timeOfDay) && isBlackTheme) {
            changeBlackTheme(false)
        }
        if (!isDayTime(timeOfDay) && !isBlackTheme) {
            changeBlackTheme(true)
        }
    }

    private fun changeBlackTheme(blackTheme: Boolean) {
        var textColor = Color.WHITE
        var pathColor = Color.WHITE

        if (blackTheme) {
            mainFrameLayout.setBackgroundResource(R.drawable.gradient)
            centerLineImageView.setImageResource(R.drawable.ic_center_line)
        } else {
            mainFrameLayout.setBackgroundResource(R.drawable.gradient_white)
            centerLineImageView.setImageResource(R.drawable.ic_center_line_day)
        }

        if (!blackTheme) {
            textColor = Color.BLACK
            pathColor = Color.BLACK
        }

        rocketTempTextView.setTextColor(textColor)
        dateTimeTextView.setTextColor(textColor)
        kmLeftTextView.setTextColor(textColor)
        powerConsumptionTextView.setTextColor(textColor)
        kmLeftText.setTextColor(textColor)
        currentTempTextView.setTextColor(textColor)

        powerConsumptionImageView.setColorFilter(pathColor)
        rocketImage.setColorFilter(pathColor)
        bottom1new.setColorFilter(pathColor)

        rocketProgressBar.progressTintList = ColorStateList.valueOf(pathColor)
        kwProgressBar.progressTintList = ColorStateList.valueOf(pathColor)
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000
    }
}
