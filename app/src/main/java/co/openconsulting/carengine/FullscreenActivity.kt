package co.openconsulting.carengine

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_fullscreen.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity() {
    private val updatePeriod = 500L //
    private val currentDateTime = Calendar.getInstance().getTime()
    private val currentBatteryTemp = 0
    private val currentMotorTemp = 0
    private val currentKmLeft = 120
    private val currentPowerConsumption = 120


    private lateinit var updateHandler : Handler
    //    Logic

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {

    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_fullscreen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        updateHandler = Handler()
        updateHandler.postDelayed({
            updateUI()
        }, updatePeriod)
    }

    // use this function to set data from outside.
    public fun setData() {

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

        // draw curved ilne
//        https://stackoverflow.com/questions/27704200/how-to-draw-a-curved-line-in-android
    }

    @SuppressLint("SimpleDateFormat")
    fun dateToString(date : Date) : String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(date)
    }

//    Chicken - Motor Temp - Range - [ 0C - 70C]  - Replace icon with motor icon
//    Rocket - Battery Temp - Range [0C - 80C] - Replace with battery icon

    private fun isMotorIcon() : Boolean {
        return currentMotorTemp > 70
    }

    private fun isBatteryIcon() : Boolean {
        return currentBatteryTemp > 80
    }

    private fun updateUI() {
        if (isMotorIcon()) {
            chickenImage.setImageResource(R.drawable.ic_power_consumption)
        } else {
            chickenImage.setImageResource(R.drawable.ic_chicken)
        }

        if (isBatteryIcon()) {
            // TODO: Update with battery icon
            rocketImage.setImageResource(R.drawable.ic_rocket_simple)
        } else {
            rocketImage.setImageResource(R.drawable.ic_rocket_simple)
        }

        // current kmLeft
        kmLeftTextView.text = currentKmLeft.toString()
        dateTimeTextView.text = dateToString(currentDateTime)
        chickenTempTextView.text = currentMotorTemp.toString()
        rocketTempTextView.text = currentBatteryTemp.toString()
        powerConsumptionTextView.text = currentPowerConsumption.toString()

        // drawing
        drawLine()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
//        fullscreen_content_controls.visibility = View.GONE
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
//        fullscreen_content.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
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

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}
