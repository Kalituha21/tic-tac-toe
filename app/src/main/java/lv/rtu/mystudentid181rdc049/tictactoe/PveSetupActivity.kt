package lv.rtu.mystudentid181rdc049.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class PveSetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pve_setup)
    }

    fun startPlayer(view: View) {
        val playerHint: EditText = findViewById(R.id.hintPlayerName)
        val player: String = playerHint.text.toString()
        val intent = Intent(this, GameActivity::class.java)

        intent.putExtra("GAME_MODE", GameModes.PVE.playersCount)
        intent.putExtra("PLAYER", player)
        intent.putExtra("STARTS", PlayerIdentities.PLAYER_1.identity)

        startActivity(intent)
    }

    fun startBot(view: View) {
        val playerHint: EditText = findViewById(R.id.hintPlayerName)
        val player: String = playerHint.text.toString()
        val intent = Intent(this, GameActivity::class.java)

        intent.putExtra("GAME_MODE", GameModes.PVE.playersCount)
        intent.putExtra("PLAYER", player)
        intent.putExtra("STARTS", PlayerIdentities.PLAYER_2.identity)

        startActivity(intent)
    }
}