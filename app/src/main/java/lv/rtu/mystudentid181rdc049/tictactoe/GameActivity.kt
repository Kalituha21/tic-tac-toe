package lv.rtu.mystudentid181rdc049.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class GameActivity : AppCompatActivity() {
    var bot: Bot = Bot(PlayerIdentities.PLAYER_2.identity)
    lateinit var gameData: GameData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        this.initializeGame()
        this.startNextTurn()
    }

    private fun initializeGame() {
        var player1: String = "Player 1"
        var player2: String = "Player 2"
        val gameMode: Int = intent.getIntExtra("GAME_MODE", GameModes.PVE.playersCount)

        if (gameMode == GameModes.PVE.playersCount) {
            val starts: Char = intent.getCharExtra("STARTS",  PlayerIdentities.PLAYER_1.identity)

            if (starts == PlayerIdentities.PLAYER_1.identity) {
                player1 = intent.getStringExtra("PLAYER") ?: player1
                player2 = "Computer"
                this.bot = Bot(PlayerIdentities.PLAYER_2.identity)
            } else {
                player1 = "Computer"
                player2 = intent.getStringExtra("PLAYER") ?: player2
                this.bot = Bot(PlayerIdentities.PLAYER_1.identity)
            }
        } else {
            player1 = intent.getStringExtra("PLAYER_1") ?: player1
            player2 = intent.getStringExtra("PLAYER_2") ?: player2
        }

        this.gameData = GameData(gameMode, player1, player2)
    }

    private fun startNextTurn() {
        this.loadBoardValues()
        val currentPlayerHolder = findViewById<TextView>(R.id.currentPlayerName)
        currentPlayerHolder.text = this.gameData.getCurrentPlayerName()
        this.botPlay()
    }

    private fun loadBoardValues() {
        val button_0_0: Button = findViewById(R.id.button_0_0)
        val button_0_1: Button = findViewById(R.id.button_0_1)
        val button_0_2: Button = findViewById(R.id.button_0_2)
        val button_1_0: Button = findViewById(R.id.button_1_0)
        val button_1_1: Button = findViewById(R.id.button_1_1)
        val button_1_2: Button = findViewById(R.id.button_1_2)
        val button_2_0: Button = findViewById(R.id.button_2_0)
        val button_2_1: Button = findViewById(R.id.button_2_1)
        val button_2_2: Button = findViewById(R.id.button_2_2)

        button_0_0.text = this.gameData.gameBoard.board[0][0].toString()
        button_0_1.text = this.gameData.gameBoard.board[0][1].toString()
        button_0_2.text = this.gameData.gameBoard.board[0][2].toString()
        button_1_0.text = this.gameData.gameBoard.board[1][0].toString()
        button_1_1.text = this.gameData.gameBoard.board[1][1].toString()
        button_1_2.text = this.gameData.gameBoard.board[1][2].toString()
        button_2_0.text = this.gameData.gameBoard.board[2][0].toString()
        button_2_1.text = this.gameData.gameBoard.board[2][1].toString()
        button_2_2.text = this.gameData.gameBoard.board[2][2].toString()
    }

    private fun botPlay() {
        if (this.gameData.mode == GameModes.PVP.playersCount) {
            return
        }

        if (this.bot.identity != this.gameData.currentPlayer) {
            return
        }

        val coordinate: Pair<Int, Int> = this.bot.getPlayCoordinates(this.gameData.gameBoard)

        Thread.sleep(240)
        this.play(coordinate.first, coordinate.second)
    }

    private fun play(row: Int, column: Int) {
        this.gameData.play(row, column)
        val winner: String? = this.gameData.getWinner()

        if (winner === null) this.startNextTurn() else this.finishGame(winner)
    }

    private fun finishGame(winner: String) {
        val intent = Intent(this, EndActivity::class.java)

        intent.putExtra("WINNER", winner)
        Thread.sleep(240)
        startActivity(intent)
    }

    fun playButton_0_0(view: View) { this.play(0, 0) }
    fun playButton_0_1(view: View) { this.play(0, 1) }
    fun playButton_0_2(view: View) { this.play(0, 2) }
    fun playButton_1_0(view: View) { this.play(1, 0) }
    fun playButton_1_1(view: View) { this.play(1, 1) }
    fun playButton_1_2(view: View) { this.play(1, 2) }
    fun playButton_2_0(view: View) { this.play(2, 0) }
    fun playButton_2_1(view: View) { this.play(2, 1) }
    fun playButton_2_2(view: View) { this.play(2, 2) }
}