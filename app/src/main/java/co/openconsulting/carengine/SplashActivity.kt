package co.openconsulting.carengine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    private val splashTime = 3000L;
    private lateinit var myHandler : Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        myHandler = Handler()
        myHandler.postDelayed({
            goToMainActivity()
        }, splashTime)

    }

    private fun goToMainActivity() {
        val mainActivity = Intent(applicationContext, FullscreenActivity::class.java)
        startActivity(mainActivity)
        finish()
    }
}
