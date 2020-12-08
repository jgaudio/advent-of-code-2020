package jgaudio.adventofcode

import java.nio.file.Files
import java.nio.file.Paths

fun day3Part1(speedX: Int, speedY: Int): Long {
    val lines = Files.readAllLines(Paths.get("src/main/resources/aoc3.txt"))
    val width = lines[0].length
    val height = lines.size

    var x = 0
    var y = 0

    var trees = 0L

    while (y < height) {
        if (lines[y][x % width] == '#') trees++
        x += speedX
        y += speedY
    }

    println("[${speedX},${speedY}]: $trees")

    return trees
}

fun day3Part2(): Long {
    return day3Part1(1, 1) * day3Part1(3, 1) * day3Part1(5, 1) * day3Part1(7, 1) * day3Part1(1, 2)
}

fun main() {
    print("Trees: ${day3Part2()}")
}