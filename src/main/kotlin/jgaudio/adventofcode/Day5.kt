package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

fun day5Part1(): Int {

    var globalMaxSeatID = 0

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc5.txt"))) {
        var lRow = 0
        var hRow = 127
        var lCol = 0
        var rCol = 7
        var row = 0
        var col = 0

        for (c in line) {
            when (c) {
                'B' -> {
                    if (hRow - lRow == 1) {
                        row = hRow
                    } else lRow += (hRow - lRow) / 2 + 1
                }
                'F' -> {
                    if (hRow - lRow == 1) {
                        row = lRow
                    } else hRow -= (hRow - lRow) / 2 + 1
                }
                'L' -> {
                    if (rCol - lCol == 1) {
                        col = lCol
                    } else rCol -= (rCol - lCol) / 2 + 1
                }
                'R' -> {
                    if (rCol - lCol == 1) {
                        col = rCol
                    } else lCol += (rCol - lCol) / 2 + 1
                }
            }
        }

        val localSeatID = row * 8 + col
        if (localSeatID > globalMaxSeatID) {
            globalMaxSeatID = localSeatID
        }
    }

    return globalMaxSeatID
}

fun day5Part2(): Int {

    val allSeatIDs = TreeSet<Int>()

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc5.txt"))) {
        var lRow = 0
        var hRow = 127
        var lCol = 0
        var rCol = 7
        var row = 0
        var col = 0

        for (c in line) {
            when (c) {
                'B' -> {
                    if (hRow - lRow == 1) {
                        row = hRow
                    } else lRow += (hRow - lRow) / 2 + 1
                }
                'F' -> {
                    if (hRow - lRow == 1) {
                        row = lRow
                    } else hRow -= (hRow - lRow) / 2 + 1
                }
                'L' -> {
                    if (rCol - lCol == 1) {
                        col = lCol
                    } else rCol -= (rCol - lCol) / 2 + 1
                }
                'R' -> {
                    if (rCol - lCol == 1) {
                        col = rCol
                    } else lCol += (rCol - lCol) / 2 + 1
                }
            }
        }

        allSeatIDs.add(row * 8 + col)
    }

    var lastVisited = 0
    for (seatID in allSeatIDs) {
        if (lastVisited > 0 && (seatID - lastVisited) > 1) {
            return seatID - 1
        } else {
            lastVisited = seatID
        }
    }
    return 0
}

fun main() {
    print("Seat ID: ${day5Part2()}")
}