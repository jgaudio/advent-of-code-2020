package jgaudio.adventofcode.day12.part2

import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs

private data class Waypoint(var position: Pair<Int, Int> = Pair(10, 1)) {

    fun rotateRight(angle: Int) {
        position = when (angle) {
            90 -> Pair(position.second, -1 * position.first)
            180 -> Pair(-1 * position.first, -1 * position.second)
            270 -> Pair(-1 * position.second, position.first)
            else -> throw IllegalArgumentException("Angle of $angle degrees")
        }
    }

    fun rotateLeft(angle: Int) {
        position = when (angle) {
            90 -> Pair(-1 * position.second, position.first)
            180 -> Pair(-1 * position.first, -1 * position.second)
            270 -> Pair(position.second, -1 * position.first)
            else -> throw IllegalArgumentException("Angle of $angle degrees")
        }
    }

    fun moveInDirection(direction: Pair<Int, Int>, value: Int) {
        position = Pair(position.first + value * direction.first,
                position.second + value * direction.second)
    }

    fun translateShipPosition(origin: Pair<Int, Int>, factor: Int): Pair<Int, Int> {
        return Pair(origin.first + position.first * factor,
                origin.second + position.second * factor)
    }
}

private data class Ship(var northSouth: Int = 0, var eastWest: Int = 0, val waypoint: Waypoint = Waypoint()) {

    fun move(instruction: Instruction) {
        when (instruction.action) {
            'F' -> {
                val translation = waypoint.translateShipPosition(Pair(eastWest, northSouth), instruction.value)
                eastWest = translation.first
                northSouth = translation.second
            }
            'N' -> waypoint.moveInDirection(Pair(0, 1), instruction.value)
            'S' -> waypoint.moveInDirection(Pair(0, -1), instruction.value)
            'E' -> waypoint.moveInDirection(Pair(1, 0), instruction.value)
            'W' -> waypoint.moveInDirection(Pair(-1, 0), instruction.value)
            'R' -> waypoint.rotateRight(instruction.value)
            'L' -> waypoint.rotateLeft(instruction.value)
        }
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

fun day12Part2(): Int {
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
    println("Manhattan distance: ${day12Part2()}")
}