package lv.rtu.mystudentid181rdc049.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun playPve(view: View) {
        startActivity(Intent(this, PveSetupActivity::class.java))
    }

    fun playPvp(view: View) {
        startActivity(Intent(this, PvpSetupActivity::class.java))
    }

    fun exit(view: View) {
        finishAffinity()
    }
}