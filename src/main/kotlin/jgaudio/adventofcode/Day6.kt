package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths
import java.util.function.IntConsumer

fun day6Part1() : Int {

    var globalTotal = 0

    val groupSubTotal = mutableSetOf<Int>()

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc6.txt"))) {
        if (line.isBlank()) {
            globalTotal += groupSubTotal.size
            groupSubTotal.clear()
        } else {
            line.chars().forEach(groupSubTotal::add)
        }
    }

    if (groupSubTotal.isNotEmpty()) {
        globalTotal += groupSubTotal.size
    }

    return globalTotal
}

fun day6Part2() : Int {

    var globalTotal = 0

    val groupSubTotal = mutableMapOf<Int, Int>()
    var groupSize = 0

    for (line in Files.readAllLines(Paths.get("src/main/resources/aoc6.txt"))) {
        if (line.isBlank()) {
            globalTotal += groupSubTotal.filter { e -> e.value == groupSize }.size
            groupSubTotal.clear()
            groupSize = 0
        } else {
            line.chars().forEach { i -> groupSubTotal.compute(i) { _, curr -> if (curr != null) curr+1 else 1} }
            groupSize++
        }
    }

    if (groupSubTotal.isNotEmpty()) {
        globalTotal += groupSubTotal.filter { e -> e.value == groupSize }.size
    }

    return globalTotal
}

fun main() {
    print("Sum: ${day6Part2()}")
}