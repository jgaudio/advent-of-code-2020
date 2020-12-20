package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths

private const val FLOOR = '.'
private const val EMPTY = 'L'
private const val OCCUPIED = '#'

private data class Seats(val seats: String, val width: Int, val height: Int) {
    fun noAdjacentMatch(row: Int, col: Int, state: Char): Boolean {
        return getAdjacentIndexes(row, col).none { it >= 0 && it < seats.length && seats[it] != FLOOR && seats[it] == state }
    }

    fun adjacentMatchCount(row: Int, col: Int, state: Char): Int {
        return getAdjacentIndexes(row, col).count { it >= 0 && it < seats.length && seats[it] == state }
    }

    private fun getAdjacentIndexes(row: Int, col: Int): List<Int> {
        val indexes = mutableListOf<Int>()
        if (row > 0 && col > 0) indexes.add((row - 1) * width + col - 1)
        if (row > 0 && col < width - 1) indexes.add((row - 1) * width + col + 1)
        if (row > 0) indexes.add((row - 1) * width + col)
        if (row < height - 1) indexes.add((row + 1) * width + col)
        if (row < height - 1 && col > 0) indexes.add((row + 1) * width + col - 1)
        if (row < height - 1 && col < width - 1) indexes.add((row + 1) * width + col + 1)
        if (col > 0) indexes.add(row * width + col - 1)
        if (col < width - 1) indexes.add(row * width + col + 1)
        return indexes
    }

    override fun toString(): String {
        return seats.chunked(width).joinToString(separator = "\n")
    }
}

private data class SeatInspection(var empty: MutableList<Int> = ArrayList(),
                                  var occupied: MutableList<Int> = ArrayList()) {
    fun hasChanges(): Boolean {
        return empty.isNotEmpty() || occupied.isNotEmpty()
    }
}

private fun inspectSeats(seats: Seats): SeatInspection {
    val inspection = SeatInspection()
    for (row in 0 until seats.height) {
        for (col in 0 until seats.width) {
            val seat = row * seats.width + col
            when (seats.seats[seat]) {
                EMPTY -> if (seats.noAdjacentMatch(row, col, OCCUPIED)) inspection.occupied.add(seat)
                OCCUPIED -> if (seats.adjacentMatchCount(row, col, OCCUPIED) >= 4) inspection.empty.add(seat)
            }
        }
    }
    return inspection
}

private fun applyInspection(inspection: SeatInspection, seats: Seats): Seats {
    val chars = seats.seats.toCharArray()
    inspection.empty.forEach { chars[it] = EMPTY }
    inspection.occupied.forEach { chars[it] = OCCUPIED }
    return Seats(String(chars), seats.width, seats.height)
}

fun day11Part1(): Int {
    var seats = input()
    var counter = 0
    do {
        val inspection = inspectSeats(seats)
        seats = applyInspection(inspection, seats)
        println(seats)
        println()
        if (!inspection.hasChanges()) counter++
    } while (counter < 5)
    return seats.seats.count { it == OCCUPIED }
}

private fun input(): Seats {
    val lines = Files.readAllLines(Paths.get("src/main/resources/aoc11.txt"))
    val width = lines[0].length
    val height = lines.size
    return Seats(lines.joinToString(separator = ""), width, height)
}

fun main() {
    println("Seats occupied: ${day11Part1()}")
}