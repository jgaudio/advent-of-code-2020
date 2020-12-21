package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs

private data class Ship(var northSouth: Int = 0, var eastWest: Int = 0, var currentDirection: Pair<Int, Int> = Pair(0, 1)) {
    fun move(instruction: Instruction) {
        when (instruction.action) {
            'F' -> moveInDirection(currentDirection, instruction.value)
            'N' -> moveInDirection(Pair(1, 0), instruction.value)
            'S' -> moveInDirection(Pair(-1, 0), instruction.value)
            'E' -> moveInDirection(Pair(0, 1), instruction.value)
            'W' -> moveInDirection(Pair(0, -1), instruction.value)
            'R' -> rotateRight(instruction.value)
            'L' -> rotateLeft(instruction.value)
        }
    }

    private fun rotateRight(angle: Int) {
        var times = angle / 90
        while (times > 0) {
            currentDirection = when (currentDirection) {
                Pair(1, 0) -> Pair(0, 1)
                Pair(0, 1) -> Pair(-1, 0)
                Pair(-1, 0) -> Pair(0, -1)
                Pair(0, -1) -> Pair(1, 0)
                else -> currentDirection
            }
            times--
        }
    }

    private fun rotateLeft(angle: Int) {
        var times = angle / 90
        while (times > 0) {
            currentDirection = when (currentDirection) {
                Pair(1, 0) -> Pair(0, -1)
                Pair(0, -1) -> Pair(-1, 0)
                Pair(-1, 0) -> Pair(0, 1)
                Pair(0, 1) -> Pair(1, 0)
                else -> currentDirection
            }
            times--
        }
    }

    private fun moveInDirection(direction: Pair<Int, Int>, value: Int) {
        northSouth += value * direction.first
        eastWest += value * direction.second
    }

    fun manhattanDistance(): Int {
        return abs(northSouth) + abs(eastWest)
    }

}

private data class Instruction(val action: Char, val value: Int)

private fun parseInstruction(line: String): Instruction {
    val action = line[0]
    return Instruction(action, line.substringAfter(action).toInt())
}

fun day12Part1(): Int {
    val lines = input()
    val ship = Ship()
    for (line in lines) {
        val instruction = parseInstruction(line)
        ship.move(instruction)
    }
    return ship.manhattanDistance()
}

private fun input() = Files.readAllLines(Paths.get("src/main/resources/aoc12.txt"))

fun main() {
    println("Manhattan distance: ${day12Part1()}")
}