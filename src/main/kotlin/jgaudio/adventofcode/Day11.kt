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

    fun noMatchInAllDirections(row: Int, col: Int, state: Char): Boolean {
        return lookInAllDirections(row, col).none { it == state }
    }

    fun allDirectionsCount(row: Int, col: Int, state: Char): Int {
        return lookInAllDirections(row, col).count { it == state }
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

    private fun lookInAllDirections(row: Int, col: Int): List<Char> {
        val sightedSeats = mutableListOf<Char>()
        addSightedSeat(sightedSeats, lookLeft(row, col))
        addSightedSeat(sightedSeats, lookRight(row, col))
        addSightedSeat(sightedSeats, lookUp(row, col))
        addSightedSeat(sightedSeats, lookDown(row, col))
        addSightedSeat(sightedSeats, lookLeftUp(row, col))
        addSightedSeat(sightedSeats, lookRightUp(row, col))
        addSightedSeat(sightedSeats, lookLeftDown(row, col))
        addSightedSeat(sightedSeats, lookRightDown(row, col))
        return sightedSeats
    }

    private fun lookRightDown(row: Int, col: Int): Char? {
        if (row < height - 1 && col < width - 1) {
            var r = row + 1
            var c = col + 1
            while (r < height && c < width) {
                val s = seats[r * width + c]
                if (s != FLOOR) return s
                r++
                c++
            }
        }
        return null
    }

    private fun lookLeftDown(row: Int, col: Int): Char? {
        if (row < height - 1 && col > 0) {
            var r = row + 1
            var c = col - 1
            while (r < height && c >= 0) {
                val s = seats[r * width + c]
                if (s != FLOOR) return s
                r++
                c--
            }
        }
        return null
    }

    private fun lookRightUp(row: Int, col: Int): Char? {
        if (row > 0 && col < width - 1) {
            var r = row - 1
            var c = col + 1
            while (r >= 0 && c < width) {
                val s = seats[r * width + c]
                if (s != FLOOR) return s
                r--
                c++
            }
        }
        return null
    }

    private fun lookLeftUp(row: Int, col: Int): Char? {
        if (row > 0 && col > 0) {
            var r = row - 1
            var c = col - 1
            while (r >= 0 && c >= 0) {
                val s = seats[r * width + c]
                if (s != FLOOR) return s
                r--
                c--
            }
        }
        return null
    }

    private fun lookDown(row: Int, col: Int): Char? {
        if (row < height - 1) {
            for (i in row + 1 until height) {
                val c = seats[i * width + col]
                if (c != FLOOR) return c
            }
        }
        return null
    }

    private fun lookUp(row: Int, col: Int): Char? {
        if (row > 0) {
            for (i in row - 1 downTo 0) {
                val c = seats[i * width + col]
                if (c != FLOOR) return c
            }
        }
        return null
    }

    private fun lookRight(row: Int, col: Int): Char? {
        if (col < width - 1) {
            for (i in col + 1 until width) {
                val c = seats[row * width + i]
                if (c != FLOOR) return c
            }
        }
        return null
    }

    private fun lookLeft(row: Int, col: Int): Char? {
        if (col > 0) {
            for (i in col - 1 downTo 0) {
                val c = seats[row * width + i]
                if (c != FLOOR) return c
            }
        }
        return null
    }

    private fun addSightedSeat(sightedSeats: MutableList<Char>, seat: Char?) {
        if (seat != null) sightedSeats.add(seat)
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

private fun inspectSeatsEnhanced(seats: Seats): SeatInspection {
    val inspection = SeatInspection()
    for (row in 0 until seats.height) {
        for (col in 0 until seats.width) {
            val seat = row * seats.width + col
            when (seats.seats[seat]) {
                EMPTY -> if (seats.noMatchInAllDirections(row, col, OCCUPIED)) inspection.occupied.add(seat)
                OCCUPIED -> if (seats.allDirectionsCount(row, col, OCCUPIED) >= 5) inspection.empty.add(seat)
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
    do {
        val inspection = inspectSeats(seats)
        seats = applyInspection(inspection, seats)
        println(seats)
        println()
    } while (inspection.hasChanges())
    return seats.seats.count { it == OCCUPIED }
}

fun day11Part2(): Int {
    var seats = input()
    do {
        val inspection = inspectSeatsEnhanced(seats)
        seats = applyInspection(inspection, seats)
        println(seats)
        println()
    } while (inspection.hasChanges())
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
    println("Seats occupied (Part 2): ${day11Part2()}")
}