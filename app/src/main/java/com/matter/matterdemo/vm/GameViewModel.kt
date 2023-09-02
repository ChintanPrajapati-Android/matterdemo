package com.matter.matterdemo.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matter.matterdemo.model.SavedData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Arrays
import java.util.Hashtable

class GameViewModel : ViewModel() {
    private var gameActive = true
    private var activePlayer = 0
    private var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    private var winPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )
    private var counter = 0
    private val _setData = MutableStateFlow<Hashtable<Int, SavedData>>(Hashtable())
    val setData = _setData.asStateFlow()


    fun onTapped(clickPosition: Int) {
        if (!gameActive) {
            reset()
        } else {
            val htClickData = Hashtable<Int, SavedData>()
            if (gameState[clickPosition] == 2) {
                counter++
                if (counter == 9) {
                    gameActive = false
                }
                gameState[clickPosition] = activePlayer
                if (activePlayer == 0) {
                    activePlayer = 1
                    htClickData[clickPosition] = SavedData(label = "X", status = "O's Turn")
                } else {
                    activePlayer = 0
                    htClickData[clickPosition] = SavedData(label = "O", status = "X's Turn")
                }
            }
            var flag = 0
            if (counter > 4) {
                for (winPosition in winPositions) {
                    if (gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] && gameState[winPosition[0]] != 2) {
                        flag = 1
                        gameActive = false
                        val winnerStr: String = if (gameState[winPosition[0]] == 0) {
                            "X has won"
                        } else {
                            "O has won"
                        }
                        htClickData[-1] = SavedData(label = "", status = winnerStr)
                    }
                }
                if (counter == 9 && flag == 0) {
                    htClickData[-1] = SavedData(label = "", status = "Match Draw")
                }
            }
            viewModelScope.launch {
                _setData.emit(htClickData)
            }
        }
    }

    fun reset() {
        val htClickData = Hashtable<Int, SavedData>()
        gameActive = true
        activePlayer = 0
        Arrays.fill(gameState, 2)
        htClickData[-2] = SavedData(label = "", status = "X's Turn - Tap to play")
        viewModelScope.launch {
            _setData.emit(htClickData)
        }
        counter = 0
    }
}