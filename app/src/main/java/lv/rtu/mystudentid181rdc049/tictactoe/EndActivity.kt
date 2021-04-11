package lv.rtu.mystudentid181rdc049.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class EndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end)

        val winner: String = intent.getStringExtra("Winner") ?: ""
        val winnerHolder = findViewById<TextView>(R.id.winnerName)
        winnerHolder.text = winner
    }

    fun goToMain(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
    }
}