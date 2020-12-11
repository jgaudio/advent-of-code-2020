package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val numbers = Files.readAllLines(Paths.get("src/main/resources/aoc9.txt")).map { it.toLong() }
private const val BUFFER_SIZE = 25

fun day9Part1(): Long {
    var sorted = populateInitialSorted()

    for (index in BUFFER_SIZE until numbers.size) {

        val n = numbers[index]

        var hasProperty = false
        var previousIdx = index - BUFFER_SIZE

        while (previousIdx < index && !hasProperty) {
            val target = n - numbers[previousIdx]
            hasProperty = sorted.contains(target)
            previousIdx++
        }

        if (!hasProperty) {
            return n
        } else {

            sorted.remove(numbers[index - BUFFER_SIZE])
            sorted.add(n)
        }
    }

    return -1
}

fun day9Part2(target:Long): Long {

    val addends = mutableListOf<Long>()
    var total = 0L

    for (index in 0..numbers.size) {
        val n = numbers[index]
        while (total + n > target && addends.isNotEmpty()) {
            total -= addends[0]
            addends.removeAt(0)
        }
        addends.add(n)
        total += n

        if (total == target) {
            addends.sort()
            return addends[0] + addends[addends.size - 1]
        }
    }

    return -1
}

fun populateInitialSorted(): MutableSet<Long> {
    val sorted = TreeSet<Long>()
    for (i in 0 until BUFFER_SIZE) {
        sorted.add(numbers[i])
    }
    return sorted
}

fun main() {
    val target = day9Part1()
    println("Invalid number: $target")
    println("XMAS weakness: ${day9Part2(target)}")
}


