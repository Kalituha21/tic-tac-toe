package lv.rtu.mystudentid181rdc049.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class PvpSetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pvp_setup)
    }

    fun startGame(view: View) {
        val player1Hint: EditText = findViewById(R.id.hintPlayer1Name)
        val player1: String = player1Hint.text.toString()
        val player2Hint: EditText = findViewById(R.id.hintPlayer2Name)
        val player2: String = player2Hint.text.toString()
        val intent = Intent(this, GameActivity::class.java)

        intent.putExtra("GAME_MODE", GameModes.PVP.playersCount)
        intent.putExtra("PLAYER_1", player1)
        intent.putExtra("PLAYER_2", player2)

        startActivity(intent)
    }
}