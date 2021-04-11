package lv.rtu.mystudentid181rdc049.tictactoe

enum class GameModes(val playersCount: Int) {
    PVE(1), PVP(2)
}

enum class PlayerIdentities(val identity: Char) {
    PLAYER_1('X'), PLAYER_2('O'), NO_VALUE(' ')
}

data class GameBoard(val board: MutableList<MutableList<Char>> = mutableListOf(
        mutableListOf(
                PlayerIdentities.NO_VALUE.identity,
                PlayerIdentities.NO_VALUE.identity,
                PlayerIdentities.NO_VALUE.identity
        ),
        mutableListOf(
                PlayerIdentities.NO_VALUE.identity,
                PlayerIdentities.NO_VALUE.identity,
                PlayerIdentities.NO_VALUE.identity
        ),
        mutableListOf(
                PlayerIdentities.NO_VALUE.identity,
                PlayerIdentities.NO_VALUE.identity,
                PlayerIdentities.NO_VALUE.identity
        )
))

class GameData (
    val mode: Int,
    private val player1: String = "Player 1",
    private val player2: String = "Player 2",
    var currentPlayer: Char =  PlayerIdentities.PLAYER_1.identity,
    val gameBoard: GameBoard = GameBoard()
) {
    private fun switchPlayer() {
        if (this.currentPlayer == PlayerIdentities.PLAYER_2.identity) {
            this.currentPlayer = PlayerIdentities.PLAYER_1.identity
        } else {
            this.currentPlayer = PlayerIdentities.PLAYER_2.identity
        }
    }

    fun getCurrentPlayerName(): String = if (this.currentPlayer == PlayerIdentities.PLAYER_1.identity) this.player1 else this.player2

    fun play(row: Int, column: Int) {
        if (this.gameBoard.board[row][column] != PlayerIdentities.NO_VALUE.identity) {
            return
        }

        this.gameBoard.board[row][column] = this.currentPlayer
        this.switchPlayer()
    }

    fun getWinner(): String? {
        val scores = mutableMapOf<Char, Int>(PlayerIdentities.NO_VALUE.identity to 0)

        // check winner by rows
        for (row in 0..2) {
            scores[PlayerIdentities.PLAYER_1.identity] = 0
            scores[PlayerIdentities.PLAYER_2.identity] = 0
            for (column in 0..2) {
                scores[this.gameBoard.board[row][column]] = scores[this.gameBoard.board[row][column]]?.plus(1) ?: 1

                if (scores[PlayerIdentities.PLAYER_1.identity] == 3) {
                    return this.player1
                }

                if (scores[PlayerIdentities.PLAYER_2.identity] == 3) {
                    return this.player2
                }
            }
        }

        // check winner by columns
        for (column in 0..2) {
            scores[PlayerIdentities.PLAYER_1.identity] = 0
            scores[PlayerIdentities.PLAYER_2.identity] = 0
            for (row in 0..2) {
                scores[this.gameBoard.board[row][column]] = scores[this.gameBoard.board[row][column]]?.plus(1) ?: 1

                if (scores[PlayerIdentities.PLAYER_1.identity] == 3) {
                    return this.player1
                }

                if (scores[PlayerIdentities.PLAYER_2.identity] == 3) {
                    return this.player2
                }
            }
        }

        // check by diagonals
        if (
                (this.gameBoard.board[1][1] == this.gameBoard.board[0][0] && this.gameBoard.board[1][1] == this.gameBoard.board[2][2])
                || (this.gameBoard.board[1][1] == this.gameBoard.board[0][2] && this.gameBoard.board[1][1] == this.gameBoard.board[2][0])
        ) {
            scores[this.gameBoard.board[1][1]] = 3

            if (scores[PlayerIdentities.PLAYER_1.identity] == 3) {
                return this.player1
            }

            if (scores[PlayerIdentities.PLAYER_2.identity] == 3) {
                return this.player2
            }
        }


        if (scores[PlayerIdentities.NO_VALUE.identity] == 0) return "Dead heat"

        return null
    }
}

class Bot(val identity: Char) {
    private val enemyIdentity: Char = if (this.identity == PlayerIdentities.PLAYER_1.identity) PlayerIdentities.PLAYER_2.identity else PlayerIdentities.PLAYER_1.identity

    fun getPlayCoordinates(currentBoard: GameBoard): Pair<Int, Int> {
        var playRow: Int? = null
        var playColumn: Int? = null
        var playCoordinates: Pair<Int, Int>? = null

        // finds and returns if exist win coordinate
        val ownHighScoreRow = this.getHighScoreRow(currentBoard, this.identity)
        val ownHighScoreColumn = this.getHighScoreColumn(currentBoard, this.identity)
        val ownHighScoreDiagonal = this.getHighScoreDiagonal(currentBoard, this.identity)

        if (ownHighScoreRow != null) {
            playColumn = this.getFirstEmptyColumnInRow(currentBoard, ownHighScoreRow)

            if (playColumn != null) {
                return Pair(ownHighScoreRow, playColumn)
            }
        }
        if (ownHighScoreColumn != null) {
            playRow = this.getFirstEmptyRowInColumn(currentBoard, ownHighScoreColumn)

            if (playRow != null) {
                return Pair(playRow, ownHighScoreColumn)
            }
        }
        if (ownHighScoreDiagonal != null) {
            playCoordinates = this.getFirstEmptyCoordinatesInDiagonal(currentBoard, ownHighScoreDiagonal)

            if (playCoordinates != null) {
                return playCoordinates
            }
        }

        // finds and returns if exist coordinate to block enemy win
        val enemyHighScoreRow = this.getHighScoreRow(currentBoard, this.enemyIdentity)
        val enemyHighScoreColumn = this.getHighScoreColumn(currentBoard, this.enemyIdentity)
        val enemyHighScoreDiagonal = this.getHighScoreDiagonal(currentBoard, this.enemyIdentity)

        if (enemyHighScoreRow != null) {
            playColumn = this.getFirstEmptyColumnInRow(currentBoard, enemyHighScoreRow)

            if (playColumn != null) {
                return Pair(enemyHighScoreRow, playColumn)
            }
        }
        if (enemyHighScoreColumn != null) {
            playRow = this.getFirstEmptyRowInColumn(currentBoard, enemyHighScoreColumn)

            if (playRow != null) {
                return Pair(playRow, enemyHighScoreColumn)
            }
        }
        if (enemyHighScoreDiagonal != null) {
            playCoordinates = this.getFirstEmptyCoordinatesInDiagonal(currentBoard, enemyHighScoreDiagonal)

            if (playCoordinates != null) {
                return playCoordinates
            }
        }

        // finds and returns if exist possible to win coordinate
        val possibleWinRows = this.getPossibleWinRows(currentBoard, this.enemyIdentity)
        val possibleWinColumns = this.getPossibleWinColumns(currentBoard, this.enemyIdentity)
        val possibleWinDiagonals = this.getPossibleWinDiagonals(currentBoard, this.enemyIdentity)

        if (possibleWinRows.isNotEmpty()) {
            playRow = possibleWinRows.first()
            playColumn = this.getFirstEmptyColumnInRow(currentBoard, playRow)

            if (playColumn != null) {
                return Pair(playRow, playColumn)
            }
        }
        if (possibleWinColumns.isNotEmpty()) {
            playColumn = possibleWinColumns.first()
            playRow = this.getFirstEmptyRowInColumn(currentBoard, playColumn)

            if (playRow != null) {
                return Pair(playRow, playColumn)
            }
        }
        if (possibleWinDiagonals.isNotEmpty()) {
            playCoordinates = this.getFirstEmptyCoordinatesInDiagonal(currentBoard, possibleWinDiagonals.first())

            if (playCoordinates != null) {
                return playCoordinates
            }
        }
        
        // returns random empty coordinate
        for (row in 0..2) {
            for (column in 0..2) {
                if (currentBoard.board[row][column] == PlayerIdentities.NO_VALUE.identity) {
                    return Pair(row, column)
                }
            }
        }

        throw Exception("Game board is full")
    }

    private fun getHighScoreRow(gameBoard: GameBoard, checkIdentity: Char): Int? {
        for (row in 0..2) {
            var rowScore: Int = 0

            for (column in 0..2) {
                if (gameBoard.board[row][column] == checkIdentity) {
                    rowScore++
                }

                if (rowScore > 1) {
                    return row
                }
            }
        }

        return null
    }

    private fun getHighScoreColumn(gameBoard: GameBoard, checkIdentity: Char): Int? {
        for (column in 0..2) {
            var columnScore: Int = 0

            for (row in 0..2) {
                if (gameBoard.board[row][column] == checkIdentity) {
                    columnScore++
                }

                if (columnScore > 1) {
                    return column
                }
            }
        }

        return null
    }

    private fun getHighScoreDiagonal(gameBoard: GameBoard, checkIdentity: Char): Int? {
        var diagonalScore: Int = 0
        diagonalScore += if (gameBoard.board[0][0] == checkIdentity) 1 else 0
        diagonalScore += if (gameBoard.board[1][1] == checkIdentity) 1 else 0
        diagonalScore += if (gameBoard.board[2][2] == checkIdentity) 1 else 0

        if (diagonalScore > 1) {
            return 0
        }

        diagonalScore = 0
        diagonalScore += if (gameBoard.board[0][2] == checkIdentity) 1 else 0
        diagonalScore += if (gameBoard.board[1][1] == checkIdentity) 1 else 0
        diagonalScore += if (gameBoard.board[2][0] == checkIdentity) 1 else 0

        if (diagonalScore > 1) {
            return 1
        }

        return null
    }

    private fun getPossibleWinRows(gameBoard: GameBoard, enemyIdentity: Char): List<Int> {
        val possibleWinRows: List<Int> = listOf(0, 1, 2)
        return possibleWinRows.filter { gameBoard.board[it][0] != enemyIdentity && gameBoard.board[it][1] != enemyIdentity && gameBoard.board[it][2] != enemyIdentity}
    }

    private fun getPossibleWinColumns(gameBoard: GameBoard, enemyIdentity: Char): List<Int> {
        val possibleWinColumns: List<Int> = listOf(0, 1, 2)
        return possibleWinColumns.filter { gameBoard.board[0][it] != enemyIdentity && gameBoard.board[1][it] != enemyIdentity && gameBoard.board[2][it] != enemyIdentity}
    }

    private fun getPossibleWinDiagonals(gameBoard: GameBoard, enemyIdentity: Char): MutableList<Int> {
        val possibleWinDiagonals: MutableList<Int> = mutableListOf()

        if (gameBoard.board[0][0] != enemyIdentity && gameBoard.board[1][1] != enemyIdentity && gameBoard.board[2][2] != enemyIdentity) {
            possibleWinDiagonals.add(0)
        }

        if (gameBoard.board[0][2] != enemyIdentity && gameBoard.board[1][1] != enemyIdentity && gameBoard.board[2][0] != enemyIdentity) {
            possibleWinDiagonals.add(1)
        }

        return possibleWinDiagonals
    }
    
    private fun getFirstEmptyColumnInRow(gameBoard: GameBoard, row: Int): Int? {
        for (column in 0..2) {
            if (gameBoard.board[row][column] == PlayerIdentities.NO_VALUE.identity) {
                return column
            }
        }
        
        return null
    }
    
    private fun getFirstEmptyRowInColumn(gameBoard: GameBoard, column: Int): Int? {
        for (row in 0..2) {
            if (gameBoard.board[row][column] == PlayerIdentities.NO_VALUE.identity) {
                return row
            }
        }
        
        return null
    }
    
    private fun getFirstEmptyCoordinatesInDiagonal(gameBoard: GameBoard, diagonal: Int): Pair<Int, Int>? {
        if (diagonal == 0) {
            if (gameBoard.board[0][0] == PlayerIdentities.NO_VALUE.identity) return Pair(0, 0)
            if (gameBoard.board[1][1] == PlayerIdentities.NO_VALUE.identity) return Pair(1, 1)
            if (gameBoard.board[2][2] == PlayerIdentities.NO_VALUE.identity) return Pair(2, 2)
        }

        if (diagonal == 1) {
            if (gameBoard.board[0][2] == PlayerIdentities.NO_VALUE.identity) return Pair(0, 2)
            if (gameBoard.board[1][1] == PlayerIdentities.NO_VALUE.identity) return Pair(1, 1)
            if (gameBoard.board[2][0] == PlayerIdentities.NO_VALUE.identity) return Pair(2, 0)
        }
        
        return null
    }
}

