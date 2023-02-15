package com.example.unblockme.game.domain

import android.annotation.SuppressLint
import android.os.Handler
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.unblockme.game.model.Blocks
import com.example.unblockme.game.model.Coordinates
import com.example.unblockme.game.model.MainBlock
import java.io.File
import java.util.*

const val FirstLevel = 1
const val LastLevel = 3
const val defaultCacheValue = "--\n--\n--"

@SuppressLint("SdCardPath")
const val defaultPath = "/data/user/0/com.example.unblockme/files/cache"

// Manages games states, moves and levels
object GameManager {
    val isSuccessShown = mutableStateOf(false)

    // Holds the current level id
    val currentLevel = mutableStateOf(FirstLevel)

    // Holds the current move count
    val currentMoveCount = mutableStateOf(0)

    // Holds the current game state (blocks coordinates)
    val currentState: MutableState<Blocks> = mutableStateOf(getLevelInitialState())

    // Holds the level current states (for undo and reset)
    private val gameStates = Stack<Blocks>()


    // Converts the a level id to a level index
    private fun getLevelIndex(level: Int): Int {
        return level - 1
    }

    // Gets the current level layout from the level id
    private fun getLevelInitialState(level: Int = currentLevel.value): Blocks {
        return LevelLayouts[getLevelIndex(level)]
    }

    // Push the first level layout as the first state
    init {
        gameStates.push(currentState.value)
    }

    // Guard for the reset button
    fun canClear(): Boolean {
        return currentMoveCount.value > 0
    }

    // Clears the current level stored game states
    // Resets the move counter to 0
    fun clear() {
        if (!canClear()) {
            return
        }
        gameStates.clear()
        gameStates.push(getLevelInitialState())
        currentState.value = gameStates.peek()
        currentMoveCount.value = 0
    }

    // Guard for the undo button
    fun canPop(): Boolean {
        return currentMoveCount.value > 0
    }

    // Retrieves the previous state
    // Decrements the move counter
    fun pop() {
        if (!canPop()) {
            return
        }
        gameStates.pop()
        currentState.value = gameStates.peek()
        currentMoveCount.value--
    }

    // Adds a new state
    // Increments the move counter
    fun push(blocks: Blocks) {
        gameStates.push(blocks)
        currentState.value = gameStates.peek()
        currentMoveCount.value++

        // Check if the red block has escaped / win condition check
        val redBlock = currentState.value.filterIsInstance<MainBlock>()
        if (redBlock.isNotEmpty() && redBlock[0].containsCoordinate(Coordinates(5, 2))) {
            saveToCache(getLevelIndex(currentLevel.value), currentMoveCount.value)
            openSuccessDialog()
            selectNextLevel()
        }
    }

    // Guard for the next level selection
    fun canSelectNextLevel(): Boolean {
        return currentLevel.value < LastLevel
    }

    // Guard for the previous level selection
    fun canSelectPreviousLevel(): Boolean {
        return currentLevel.value > FirstLevel
    }

    // Increments the level id
    // Setup the next level
    fun selectNextLevel() {
        if (!canSelectNextLevel()) {
            return
        }
        currentLevel.value++
        setCurrentLevel()
    }

    // Decrements the level id
    // Setup the previous level
    fun selectPreviousLevel() {
        if (!canSelectPreviousLevel()) {
            return
        }
        currentLevel.value--
        setCurrentLevel()
    }

    // Clears the previous level stored states
    // Loads the current level initial state
    private fun setCurrentLevel(level: Int = currentLevel.value) {
        if (level !in FirstLevel..LastLevel) {
            return
        }
        clear()
        gameStates.push(getLevelInitialState())
        currentState.value = gameStates.peek()
    }

    var bestScores = mutableListOf("--", "--", "--")

    fun getCurrentBestScore(): String {
        return bestScores[currentLevel.value - 1]
    }

    fun getCurrentMin(): String {
        val minScores = mutableListOf("15", "17", "15")
        return minScores[currentLevel.value - 1]
    }

    fun saveToCache(level: Int, score: Int) {
        var file = getCache()
        var oldScores = file.readLines().toMutableList()
        // Only save if new score is better
        if (oldScores[level] == "--" || oldScores[level].toInt() > score) {
            oldScores[level] = score.toString()
            var newScores: String = ""
            for (score in oldScores) {
                newScores += score + "\n"
            }
            file.writeText(newScores)
            readCache()
        }
    }

    fun readCache() {
        var file = getCache()
        val currentScores = file.readLines().toMutableList()
        bestScores = currentScores
    }

    fun resetCache() {
        var file = getCache()
        file.writeText(defaultCacheValue)
        readCache()
    }

    fun setBestCache() {
        var file = getCache()
        file.writeText("15\n17\n15")
        readCache()
    }

    fun getCache(): File {
        val file = File(defaultPath)
        if (!file.exists()) {
            file.createNewFile()
            file.writeText(defaultCacheValue)
        }
        return file
    }


    fun openSuccessDialog() {
        isSuccessShown.value = true

        val handler = Handler()
        handler.postDelayed({

            isSuccessShown.value = false


        }, 3000)
    }
}